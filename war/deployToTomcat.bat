
@echo off
echo   ��ʼ
dir /b /s D:\Dev\Tomcat 7.0\webapps|find /v "images" >list.txt
for /f %%i in (list.txt) do (del /f /s /q %%i)
xcopy /s /e /f /exclude:D:\Dev\EclipseWorkspace\SFLibrary\war\uncopy.txt D:\Dev\EclipseWorkspace\SFLibrary\war D:\Dev\Tomcat 7.0\webapps
echo ���!
echo. & pause
