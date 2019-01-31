# genetic-algorithms

A simple genetic algorithm to find location of the minimum, maximum or point equal to zero of the given 2D, x^4 function

Uses a few inputs to output a result:
# a, b, c, d, e:
  factors of an x^4 equation in the form of:
    a + b*x + c*x^2 + d*x^3 + e*x^4

# Resolution, population, generations:
  resolution is the power of 2 factor, used to determine to how many parts an X axis should be split into
  population is how many random points should be created at the start of the program, to simulate a biological
    diferrentiations across population
  generations are the iterations of the population, during which, Crossing Over, Mutation, and Roulette functions take place
    to determine population members having best aim function, in other words, that fit the desired solution better than other
    members
  
Some of the factors were set final, those are propablilities of the Crossing Over, and Mutation, and were given numbers based
  on the experience and 
