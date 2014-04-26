function global:Replace-Content
{
  Param([string]$filepath, [string]$rep1, [string]$rep2)
  if ( $(test-path $filepath) -ne $True )
  {
    Write-Error "���݂��Ȃ��p�X�ł�"
    return
  }
  $file_contents = $(Get-Content $filepath -encoding String) -replace $rep1, $rep2
  $file_contents | Out-File $filepath -encoding default
}


#�^�O���̂�ݒ肷��B
$tags = "5.2.0-alpha1"

$WorkDir="build"

#tag���쐬����B
git tag -a $tags -m 'build tag'
git push --tags

#��ƃf�B���N�g�����쐬����B
Remove-Item -path $WorkDir -recurse
mkdir $WorkDir

#tag����t�@�C�����G�N�X�|�[�g����B
git archive --format zip -o $WorkDir\export.zip $tags

#�G�N�X�|�[�g�����t�@�C����W�J����B
cd $WorkDir
$file=Convert-Path(".\export.zip")
$shell = New-Object -ComObject shell.application
$zip = $shell.NameSpace($file)
$dest =  $shell.NameSpace((Split-Path $file -Parent))

$dest.CopyHere($zip.Items()) 

# �o�[�W�������X�V����B
$tag_array = $tags -split "-"
$ver = $tag_array[0]
$build = $tag_array[1]

Replace-Content ENdoSnipe\build.bat "set VER=.+" "set VER=$ver"
Replace-Content ENdoSnipe\build.bat "set BUILD=.+" "set BUILD=$build"



# �r���h�����s����B
cd ENdoSnipe
.\build.bat