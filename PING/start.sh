#check if node and npm is installed, if not install it
if ! [ -x "$(command -v node)" ]; then
  echo 'Error: node is not installed.' >&2
  sudo apt-get install nodejs
  sudo apt-get install npm
fi

#launch the command 'npm install --save-dev electron' to install electron
cd brainfuck_ide
rm -rf node_modules/
rm package-lock.json
npm cache clean --force
npm install
./node_modules/.bin/electron-rebuild
