
;;
;; %%NAME%%.scm - A simple agent written in Scheme (Kawa)
;; Copyright (C) 2002 Olivier Gutknecht
;;
;; This program is free software; you can redistribute it and/or
;; modify it under the terms of the GNU General Public License
;; as published by the Free Software Foundation; either version 2
;; of the License, or any later version.
;;
;; This program is distributed in the hope that it will be useful,
;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;; GNU General Public License for more details.
;;
;; You should have received a copy of the GNU General Public License
;; along with this program; if not, write to the Free Software
;; Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.


; Sorry if the whole thing looks hacked and mixed with debug code
; but as the Scheme binding is not perfect yet, the PingPong 
; is my usual test case - Ol.

(define myCommunity "%%PROJECT%%")
(define myGroup "myGroup") ;; change the name of the group to suit your needs
(define myRole "myRole") ;; change the name of the role to suit your needs

  
(define (activate)
	(println "I am a Scheme agent")
	(let ((r (create-group #t myCommunity myGroup #!null #!null)))
		(if (eq? r 1)
		  (println (string-append "I create the group" myGroup))
		   (println "The group has already been created"))
		(println (string-append "I request the role " myRole " in group" myGroup))
		(request-role myCommunity myGroup myRole #!null)
))

(define (live)
	(behavior))
	
(define (behavior)
   (let ((m (wait-next-message)))
      (handle-message m)
      (behavior)))
      
(define (handle-message m)
	(println (string-append "received message : " (get-string m))))

(define (end)
  (println "Scheme agent ended"))

