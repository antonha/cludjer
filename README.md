# Cludjer

Simple window manager thingy built on top of 
https://github.com/sdegutis/zephyros using Clojure. 

Sets up a few bindings for nudging windows in different directions on the 
screen. Use main.clj as the script for Zephyros. Remember to 
**use TCP sockets**, as the Clojure API can not use unix sockets. 

See 
https://github.com/sdegutis/zephyros/blob/master/Docs/Clojure.md
for setup instructions.

# Install
- Install zephyros [Link](https://raw.github.com/sdegutis/zephyros/master/Builds/Zephyros-LATEST.app.tar.gz)
- Download main.clj
- Add [lein-exec "0.3.0"] to your ~/.lein/profiles.clj :user :plugins
  `{:user {:plugins [[lein-exec "0.3.0"]]}}`
- Start Zephyros
- Open Zepyhyros preferences (right-click icon in menu bar)
- Select to run on TCP port
- Put "lein exec <location of main.clj>" in the script box
