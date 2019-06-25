#!/usr/bin/env plk

(require
 '[planck.environ :refer [env]]
 '[planck.shell :refer [sh]]
 '[planck.io :as io])

(defn strip-trailing-newline
  [string]
  (let [[_ match] (re-find #"(\w+)\n" string)]
    match))

(def platform (-> (sh "uname")
                  :out
                  strip-trailing-newline))

(defn remove-trailing-newline
  [string]
  (re-find #"\S+" string))

(defn get-absolute-path
  [relative-path]
  (let [current-dir (-> (sh "pwd")
                        :out
                        remove-trailing-newline)]
    (str current-dir "/" relative-path)))

(defn symlink
  [file target]
  (let [file-path (if (string? file) file (:path file))
        absolute-file-path (get-absolute-path file-path)]
    (sh "ln" "-svf" absolute-file-path target)))

(defn configure-zsh
  []
  (println "Setting ZSH as default shell.")
  (println)
  (sh "chsh" "-s" "/bin/zsh"))

(defn install-brew-packages
  []
  (let [packages ["clojure"
                  "diff-so-fancy"
                  "git"
                  "gnupg"
                  "imagemagick"
                  "leiningen"
                  "lumo"
                  "lynx"
                  "mg"
                  "mit-scheme"
                  "n"
                  "optipng"
                  "ruby"
                  "tree"
                  "unrar"
                  "watchman"
                  "wget"
                  "yarn"
                  "zsh"
                  "zsh-completions"]]
    (println "Installing packages from Homebrew:")
    (doseq [package packages]
      (println (str "Installing " package "."))
      (sh "brew" "install" package))
    (println))
  (configure-zsh))

(defn configure-iterm
  []
  (println "Making iterm2 use synced settings.")
  (println)
  (sh
   "defaults"
   "write"
   "com.googlecode.iterm2.plist"
   "PrefsCustomFolder"
   "-string"
   (str (:home env) "/iterm2_profile"))
  (sh
   "defaults"
   "write"
   "com.googlecode.iterm2.plist"
   "LoadPrefsFromCustomFolder"
   "-bool"
   "true"))

(defn install-brew-cask-packages
  []
  (let [packages ["emacs"
                  "iterm2"
                  "java"
                  "karabiner-elements"
                  "postman"
                  "visual-studio-code"
                  "whatsapp"]]
    (println "Installing apps from Homebrew cask:")
    (doseq [package packages]
      (println (str "Installing " package "."))
      (sh "brew" "cask install" package))
    (println))
  (configure-iterm))

(defn install-snap-apps
  []
  (let [packages [{:name "code", :classic? true} ; Visual Studio Code
                  {:name "emacs", :classic? true}
                  {:name "postman", :classic? false}]]
    (println "Installing snap apps:")
    (doseq [package packages]
      (println (str "Installing " (:name package) "."))
      (if (:classic? package)
        (sh "sudo" "snap" "install" (:name package) "--classic")
        (sh "sudo" "snap" "install" (:name package))))
    (println)))

(defn install-linux-apps
  []
  (let [packages ["default-jdk"]]
    (println "Installing Linux apps:")
    (doseq [package packages]
      (println (str "Installing " package "."))
      (sh "sudo" "apt" "install" package))
    (println)))

(defn link-home-files
  []
  (let [files (io/list-files "./home")]
    (println "Linking dotfiles to home directory:")
    (doseq [file files]
      (println (str "Linking " (io/file-name file) "."))
      (symlink file (:home env)))
    (println)))

(defn link-vscode-files
  []
  (let [settings-dir
        (case platform
              "Darwin" (str
                        (:home env)
                        "/Library/Application Support/Code/User/")
              "Linux" (str
                       (:home env)
                       "/.config/Code/User/"))]
    (println "Linking VSCode settings and keybindings.")
    (println)
    (symlink "./vscode/settings.json"
             settings-dir)
    (symlink "./vscode/keybindings.json"
             settings-dir)))

(defn install-vscode-extensions
  []
  (let [extensions-list ["adamwalzer.string-converter"
                         "dbaeumer.vscode-eslint"
                         "dbankier.vscode-quick-select"
                         "esbenp.prettier-vscode"
                         "jmfirth.vsc-space-block-jumper"
                         "jpoissonnier.vscode-styled-components"
                         "kumar-harsh.graphql-for-vscode"
                         "mikestead.dotenv"
                         "mrmlnc.vscode-csscomb"
                         "ms-vscode.atom-keybindings"
                         "ms-vscode.Theme-TomorrowKit"
                         "ms-vsliveshare.vsliveshare"
                         "octref.vetur"
                         "oderwat.indent-rainbow"
                         "ryu1kn.partial-diff"
                         "silvenon.mdx"
                         "stkb.rewrap"
                         "waderyan.gitblame"
                         "yzhang.markdown-all-in-one"
                         "zengxingxin.sort-js-object-keys"]]
    (println "Installing VSCode extensions:")
    (doseq [extension extensions-list]
      (println (str "Installing " extension "."))
      (sh "code" "--install-extension" extension))
    (println)))

(defn copy-fonts
  []
  (let [fonts (io/list-files "./fonts")
        target-dir (case platform
                         "Darwin" (str (:home env) "/Library/Fonts")
                         "Linux" "/usr/share/fonts/truetype")]
    (println "Copying fonts:")
    (doseq [font fonts]
      (println (str "Copying " (io/file-name font) "."))
      (sh "cp" (:path font) target-dir))
    (println)))

(defn macos-main
  []
  (install-brew-packages)
  (install-brew-cask-packages)
  (link-home-files)
  (link-vscode-files)
  (install-vscode-extensions)
  (copy-fonts))

(defn linux-main
  []
  (install-brew-packages)
  (install-snap-apps)
  (install-linux-apps)
  (link-home-files)
  (link-vscode-files)
  (install-vscode-extensions)
  (copy-fonts))


(defn -main
  []
  (println "Installing packages, linking dotfiles, and setting up configs.")
  (println)
  (case platform
        "Darwin" (macos-main)
        "Linux" (linux-main))
  (println "And all done!"))

(set! *main-cli-fn* -main)
