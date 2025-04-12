@echo off
echo === COMPILATION DU PROJET MAVEN ===
cd /d "%~dp0"

"C:\Users\MoPiM\.jdks\openjdk-23.0.2\bin\java.exe" ^
  -Dmaven.multiModuleProjectDirectory="%cd%" ^
  "-Dmaven.home=C:\Program Files\JetBrains\IntelliJ IDEA 2024.3.3\plugins\maven\lib\maven3" ^
  "-Dclassworlds.conf=C:\Program Files\JetBrains\IntelliJ IDEA 2024.3.3\plugins\maven\lib\maven3\bin\m2.conf" ^
  "-Dmaven.ext.class.path=C:\Program Files\JetBrains\IntelliJ IDEA 2024.3.3\plugins\maven\lib\maven-event-listener.jar" ^
  -cp "C:\Program Files\JetBrains\IntelliJ IDEA 2024.3.3\plugins\maven\lib\maven3\boot\plexus-classworlds-2.8.0.jar" ^
  org.codehaus.classworlds.Launcher package

echo.
echo === GÉNÉRATION DU .EXE AVEC LAUNCH4J ===
"C:\Program Files (x86)\Launch4j\launch4j.exe" "%cd%\launch4j-config.xml"

echo.
echo ✅ Build terminé !
pause
