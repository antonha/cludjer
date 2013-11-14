(use '[leiningen.exec :only (deps)])
(deps '[[org.clojure/data.json "0.2.2"]])

(load-file "/Applications/Zephyros.app/Contents/Resources/libs/zephyros.clj")

(bind "H" ["CMD" "CTRL"] #(focus-window-left (get-focused-window)))
(bind "L" ["CMD" "CTRL"] #(focus-window-right (get-focused-window)))
(bind "J" ["CMD" "CTRL"] #(focus-window-up (get-focused-window)))
(bind "K" ["CMD" "CTRL"] #(focus-window-down (get-focused-window)))

(defn next-fix [curr comp list] 
	(let [next (first (filter #(comp %1 curr) list ))]
		(if next next curr)
	))

(defn round [n] (Math/floor  n))

(def num-splits 6)

(defn sizes [size] 
	(map #(round (* %1 (/ size num-splits))) (range 1 (inc num-splits))))

(defn next-smaller [max-size size] 
	(next-fix 
		size
		<
		(reverse (sizes max-size))))

(defn next-larger [max-size size] 
	(next-fix 
		size
		>
		(sizes max-size)))

(defn current-screen-frame [] 
	(screen-frame-without-dock-or-menu 
		(get-screen-for-window (get-focused-window))))

(defn current-screen-width [] (:w (current-screen-frame)))
(defn current-screen-height [] (:h (current-screen-frame)))
(defn current-screen-y [] (:y (current-screen-frame)))

(defn nudge [max-size is-anchor offset-key size-key offset-for-size anchor-offset-from-size]
	(let [
		current-window (get-focused-window)
		current-frame (get-frame current-window)
		]
		(set-frame current-window
			(if  (is-anchor (offset-key current-frame) (size-key current-frame))
				(let [next-size (next-smaller max-size (size-key current-frame))]
						(assoc current-frame 
							size-key next-size
							offset-key (offset-for-size next-size)
							))
				(let [new-size 
					(next-larger max-size (size-key current-frame))]
					(if new-size
					(assoc current-frame 
						size-key new-size
						offset-key (anchor-offset-from-size new-size))))))))

(defn nudge-left []
	(let [screen-width (current-screen-width)]
	(nudge 
		screen-width
		(fn  [x _] (= x 0))
 		:x :w
		(fn [_] 0)
		#(- screen-width %1))))

(defn nudge-right [] 
	(let [screen-width (current-screen-width)]
	(nudge 
		screen-width
		#(= (+ %1 %2) screen-width)
		:x :w
		#(- screen-width %1)
		(fn [_] 0))))

(defn nudge-up []
	(let [screen-height (current-screen-height)] 
		(nudge
			screen-height
			(fn  [y _] (= y (current-screen-y)))
	 		:y :h
			(fn [_] (current-screen-y))
			#(- screen-height %1))))

(defn nudge-down [] 
	(let [screen-height (current-screen-height)]
		(nudge 
			screen-height
			#(= (+ %1 %2) (+ (current-screen-y) screen-height))
			:y :h
			#(- (+ screen-height (current-screen-y)) %1)
			(fn [_] (current-screen-y)))))

(bind "H" ["CMD" "CTRL" "SHIFT"] nudge-left)
(bind "L" ["CMD" "CTRL" "SHIFT"] nudge-right)
(bind "K" ["CMD" "CTRL" "SHIFT"] nudge-up)
(bind "J" ["CMD" "CTRL" "SHIFT"] nudge-down)

(defn find-app [name] 
	(first 
		(filter 
			#(= (get-app-title %1) name)
			(get-running-apps))))

(defn focus-app [app-name] 
	(let [win (first (visible-windows-for-app 
			(find-app app-name)))]
	(if win 
		(focus-window win)
		())))

(bind "E" ["CMD" "CTRL" "SHIFT"] #(focus-app "Sublime Text"))
(bind "B" ["CMD" "CTRL" "SHIFT"] #(focus-app "Google Chrome"))
(bind "M" ["CMD" "CTRL" "SHIFT"] #(focus-app "Mail"))
(bind "C" ["CMD" "CTRL" "SHIFT"] #(focus-app "Colloquy"))
(bind "T" ["CMD" "CTRL" "SHIFT"] #(focus-app "iTerm"))


@listen-for-callbacks