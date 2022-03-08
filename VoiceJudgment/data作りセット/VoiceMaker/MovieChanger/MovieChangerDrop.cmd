@echo off

cd OriginalSamples

SET /P name="sample name : "
if not exist %name% md %name%

for %%f in (%*) do (
	ffmpeg -i %%f %%~nf.wav
	echo ^>^>^>%%~nf.wav is done
	move /Y %%~nf.wav %name%
)
pause