# Virtuallife

A proposition of an artificial life system in Java.

## Rationale

The problem is to design a universe with elementary physics and biology laws from which living beings of (hopefully) unbounded sophistication could emerge.
It's not about making something useful. It's about recreating the miracle of life.

## Approach

We implemented universes with a discreet physical (a set of points, no more structure) space in which the mineral resources are letters.
We defined the living beings as entities with basic rules for energy production, reproduction, and mutation.

In our implementation, the phenotypes of the living beings are non-deterministic automatons that form words out of the resources they have access to.
They are redeemed a certain amount of "energy" for forming "good" (sophisticated) words. A high energy increases the odds of successful reproduction.

The most interesting results were obtained by:
- integrating the English language in the chemistry of the universe as a source of irregularities.
- structuring the physical space of the universes as "niches" with different parameters, separated by low porosity membranes.

## Lessons learned

### A. On artificial life

- We thought that we could make evolution go fast because computers go fast. Actually, computers have enormously less information processing power than most living beings.
- It's not that easy to come up with a simple problem for which the optimal solution is (infinitely) complex.

### B. On programming

This was my first significant software project, and is a good example of someone reinventing the wheel when he doesn't know about wheels.

I wish I was told before starting this project :
- about [design patterns](http://c2.com/cgi/wiki?DesignPatternsBook) (I recreated several of them in this project, but it took time and thinking to get there)
- about version control
- about [*Effective Java*](http://www.amazon.com/Effective-Java-Edition-Joshua-Bloch/dp/0321356683) by Joshua Bloch
- not to do it in Java anyway, because it's not a good tool for making exotic projects from scratch. 90% of our thinking time was dedicated to incidental software complexity. We would have taken this much further with a language like Clojure or OCaml, even with the learning time.

## Code walkthrough

Yeah, it's in French... we didn't know any better at the time. Looking back on it, this code feels quite amateur anyway, not a great read.
