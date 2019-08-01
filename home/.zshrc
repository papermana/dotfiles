# Lines configured by zsh-newuser-install
unsetopt autocd beep
bindkey -e
# End of lines configured by zsh-newuser-install

# The following lines were added by compinstall
zstyle :compinstall filename '/Users/dominik/.zshrc'
autoload -Uz compinit
compinit
# End of lines added by compinstall

source "$HOME/.slimzsh/slim.zsh"

export LANG=en_US.UTF-8

# Fixes Git not working with GPG
export GPG_TTY=$(tty)

alias git='nocorrect git'
alias npm='nocorrect npm'
alias emacs='/usr/local/Cellar/emacs/25.1/bin/emacs-25.1 -nw'

alias gitroot='cd $(git rev-parse --show-toplevel)'


export PATH="$PATH:$HOME/.rvm/bin" # Add RVM to PATH for scripting
export PATH="$PATH:$HOME/Qt5.5.1/5.5/clang_64/bin" # qmake and other stuff
export PATH="$PATH:$HOME/.meteor" # meteor launcher
export PATH="$PATH:$HOME/scripts"
export PATH="$PATH:/usr/local/Caskroom/factor/0.97/factor" # factor language

# Android development variables for React Native
export ANDROID_HOME=$HOME/Library/Android/sdk
export PATH=$PATH:$ANDROID_HOME/tools
export PATH=$PATH:$ANDROID_HOME/platform-tools
