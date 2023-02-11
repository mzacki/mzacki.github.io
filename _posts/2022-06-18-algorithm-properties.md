---
layout: single
title:  "What properties should have an algorithm?"
date:   2022-06-18 12:27:11 +0200
description: Every algorithm should be like this.
categories: algorithms computer_science
tags:

- algorithm
- programming

---
For every computer programmer (and computer scientist) it is good to know
what are the properties of a well-written algorithm. Here they are - good algorithm should be:
- **discreet** - it should consist of elementary, self-explanatory steps; 
more complex functions should be handled by separated sub-algorithms containing elementary, simple steps only;
- **general** (abstract, universal) - an algorithm should solve a class of problems and not only a given, single case;
- **unambiguous** - it should clearly indicate what of its discreet parts should be executed;
- **effective** - it should return results within finite number of steps, within defined time range; an algorithm cannot run forever even if it is handling a complex problem or there is a lack or limitiation of resources;
  in edge cases, it should simply quit or return instead of running infinitely.

Only algorithms that fill these expectations can be considered correct.