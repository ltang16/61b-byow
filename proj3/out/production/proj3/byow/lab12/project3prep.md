# Project 3 Prep

**For tessellating hexagons, one of the hardest parts is figuring out where to place each hexagon/how to easily place hexagons on screen in an algorithmic way.
After looking at your own implementation, consider the implementation provided near the end of the lab.
How did your implementation differ from the given one? What lessons can be learned from it?**

Answer: My conceptual implementation (didn't have time to stay for the full live coding demo unfortunately) involved using a 
private sideLength instance variable, representing the length of one side of the hexagon. I'd create a helper method to construct one 
singular hexagon, and then another to keep track of the full "world" of 19 hexagons in the array. The single-hexagon helper method would 
also randomize the tile type for the hexagon, but maybe I'd try to keep track of what types of hexagons are around it so no two adjacent 
hexagons could be the same.

-----

**Can you think of an analogy between the process of tessellating hexagons and randomly generating a world using rooms and hallways?
What is the hexagon and what is the tesselation on the Project 3 side?**

Answer: I'd say the hexagon is the room or hallway, and tesselating them is figuring out how they'd fit together. 

-----
**If you were to start working on world generation, what kind of method would you think of writing first? 
Think back to the lab and the process used to eventually get to tessellating hexagons.**

Answer: Probably something like generating a single room or hallway of a random size -- which would be given by the constructor. 

-----
**What distinguishes a hallway from a room? How are they similar?**

Answer: Hallways are longer and narrower and connect many rooms, usually. Both need to be delineated by walls and floor tiles. 
