# YodleJugglefestSolution
Attempted solution to the Yodle Jugglefest question

JuggleFest problem for Yodle

Problem is solved by placing jugglers in their most preferred circuit, and then cutting each
circuit down to 6 jugglers by eliminating the least compatible jugglers(based on dot product of skills).
 
This process is done until all jugglers are assigned to a circuit. Each time a juggler is kicked out of
a circuit, their most preferred circuit becomes the one directly after their previously most preferred.
  
In the case that a juggler cannot get into any of their preferred circuits, they are placed based on
which of the remaining circuits with available spots has the highest dot product with their skills.


Link to the question description on Yodle's website: http://www.yodlecareers.com/programming-puzzle-juggling
