# NeuroEvoPacman

## Overview
The repository is intended to keep track of the progress of the project related to the Intelligent Systems course at Università degli Studi di Milano. 

## Info
The main idea was to model a Pac-Man player using a neural network and to train this model to succeed in playing the video game by itself by using a genetic algorithm (NEAT, see Links section). Some constraints had to be applied on the original problem (the video game rules) to simplify it and to reach some reasonable results. Several tests have been made by using different initial neural network topologies as well as different fitness functions, error functions and parameters for the adopted genetic algorithm.
Pac-Man implementation has been taken by an existing repo (see Links section) and re-adapted to the problem.

For more info, read the report inside the repository.

## Execution instructions
The project code is a little bit messy. Follow these (tested) instructions to run the code:
1. Open the project with IntelliJ IDEA or Eclipse.
2. Set Java 1.8 as JDK.
4. Set *...\NeuroEvoPacman* as working directory.
5. Run *...\NeuroPacman\NeuroEvoGame\src\gui\NewMainGui.java*.

## Known issues
There are some known issues still unsolved due to lack of time:
- **Load function is not working**: the implementation has to be adapted from the old code to the new one.
- In some cases the matches reproduced in the GUI are not representing the true simulations occurring in background: this might be caused by the different timings (the simulations and the matches are not running at the same time: the simulations are computed first, then stored in order to be reproduced in the game loop). This might cause **inconsistencies in what occurs in background and what is reproduced on screen** (e.g. sometimes pacman dies on screen but not in the simulation, or viceversa).

## Links
**Pac-Man implementation**: https://github.com/leonardo-ono/Java2DPacmanGame

**NEAT**: http://nn.cs.utexas.edu/?neat

## Demo

### Example 1

![](gifs/1.gif)

![](gifs/2.gif)

### Example 2

![](gifs/3.gif)

![](gifs/4.gif)
