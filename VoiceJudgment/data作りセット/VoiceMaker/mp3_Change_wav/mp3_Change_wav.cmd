@echo off

cd pool

for %%f in (*.mp3) do (
	ffmpeg -i %%f %%~nf.wav
	echo ^>^>^>%%~nf.wav is done
	move %%~nf.wav wav_pool
)

pause