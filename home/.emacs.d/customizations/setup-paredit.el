;; ParEdit mode settings
(with-eval-after-load "paredit"
  ;; copy a sexp in a similar way to killing it
  (define-key paredit-mode-map (kbd "M-C-k") 'paredit-copy-as-kill)
  
  ;; indent the newline on Enter, and do not on C-j
  (define-key paredit-mode-map (kbd "RET") 'paredit-newline)
  (define-key paredit-mode-map (kbd "C-j") 'newline)
  
  ;; make moving backward and forward more intuitive
  (define-key paredit-mode-map (kbd "M-a") 'paredit-backward)
  (define-key paredit-mode-map (kbd "M-e") 'paredit-forward)

  ;; make moving to indentation more intuitive/intelligent
  (define-key paredit-mode-map
    (kbd "C-a")
    (lambda ()
      "More intelligent ``move-beggining-of-line''"
      (interactive)
      (if (>= (current-indentation)
             (- (point) (line-beginning-position)))
          (beginning-of-line)
        (back-to-indentation)))))
