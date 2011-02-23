@echo off
start "@APPNAME@" javaw -Djava.library.path=lib-native/windows -Xmx1024m -Xms256m -jar lib/@JARNAME@
