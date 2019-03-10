;; javascript / html
;; (add-to-list 'auto-mode-alist '("\\.js$" . js-mode))
(add-to-list 'auto-mode-alist '("\\.jsx?$" . web-mode))
(add-hook 'js-mode-hook 'subword-mode)
(add-hook 'html-mode-hook 'subword-mode)
(add-hook 'js-mode-hook 'electric-indent-mode)
(add-hook 'js-mode-hook 'electric-pair-mode)
(add-hook 'web-mode-hook 'subword-mode)
(add-hook 'web-mode-hook 'electric-indent-mode)
(add-hook 'web-mode-hook 'electric-pair-mode)
(setq js-indent-level 2)
(setq web-mode-markup-indent-offset 2)
(setq web-mode-css-indent-offset 2)
(setq web-mode-code-indent-offset 2)
(setq web-mode-content-types-alist
      '(("jsx" . "\\.js[x]?\\'")))
(setq web-mode-extra-auto-pairs
      '(("jsx" . (("beg" "end")))))
(setq web-mode-engines-alist
      '(("reactjs" . "\\.js[x]?\\'")))
(setq web-mode-enable-auto-quoting nil)
(eval-after-load "sgml-mode"
  '(progn
     (require 'tagedit)
     (tagedit-add-paredit-like-keybindings)
     (add-hook 'html-mode-hook (lambda () (tagedit-mode 1)))))


;; configure flycheck for javascript
(add-hook 'web-mode-hook 'flycheck-mode)
(setq-default flycheck-disabled-checkers
  (append flycheck-disabled-checkers
          '(javascript-jshint)))
(flycheck-add-mode 'javascript-eslint 'web-mode)
;; use local eslint from node_modules before global
;; http://emacs.stackexchange.com/questions/21205/flycheck-with-file-relative-eslint-executable
(defun my/use-eslint-from-node-modules ()
  (let* ((root (locate-dominating-file
                (or (buffer-file-name) default-directory)
                "node_modules"))
         (eslint (and root
                      (expand-file-name "node_modules/eslint/bin/eslint.js"
                                        root))))
    (when (and eslint (file-executable-p eslint))
      (setq-local flycheck-javascript-eslint-executable eslint))))
(add-hook 'flycheck-mode-hook #'my/use-eslint-from-node-modules)


;; coffeescript
(add-to-list 'auto-mode-alist '("\\.coffee.erb$" . coffee-mode))
(add-hook 'coffee-mode-hook 'subword-mode)
(add-hook 'coffee-mode-hook 'highlight-indentation-current-column-mode)
(add-hook 'coffee-mode-hook
          (defun coffee-mode-newline-and-indent ()
            (define-key coffee-mode-map "\C-j" 'coffee-newline-and-indent)
            (setq coffee-cleanup-whitespace nil)))
(custom-set-variables
 '(coffee-tab-width 2))
