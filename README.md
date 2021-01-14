# Individual-Turn-In-Project-Basics-of-Programming
This is the first graded turn in project, that we were tasked to create.

We are tasked to implement a Game, that allows us to raise animals 
and allow for playing with 1-4 players.

This is the first draft and is subject to change - most rules apply and will not change, but some additional
rules can come to be implemented - Which is yet to be determined.

This is the base code structure and is a work in development.

**RULES:**

- At the start of the game, the player will be asked to submit a number of rounds
and a amount of players that they wish to play with. These numbers must be within the range of 5-30 and 1-4, 
  respectively. [DONE]
  
- At the start of the game, each player has a pre-determined amount of money and no animals. [DONE]

- A player, on their turn, can choose to do **ONE** of the following
    
    - Buy an amount of animals (As long as it's afforded) - Each animal has a set 
      base price associated to them [DONE]
    
    - Buy an amount of food (As long as it's afforded) - Food is bought in kilos
    and the price is defined as per kilo. 
    
      
    - Feed their animals (What kind of food is specified for each animal on an individual basis)
    
    - Attempt to breed **ONE** set of animals. There is a 50% chanse that a new animal will be
    made from doing this (This requires the same type of animal and different genders on each animal). 
      
      In case a baby animal is made, the player can name the baby/babies. Each respective
    baby has a 50:50 chanse of being Male or Female.
      
    - Sell one or more animals, the selling price is (BASE_RATE * HEALTH_VALUE) - The HEALTH_VALUE 
    is tracked during the game - starting at 100 - and decays between rounds by 10-30 units of % [DONE]
      
      I.e, 100 (OLD_VALUE) - 23 (DECAY) = 77 (NEW_HEALTH_VALUE). To replenish the
      HEALTH_VALUE - a player must feed their animals. 
      
    - Each player gets to do actions from **ONE** of the categories above, then their turn is over.
    
- If an animal reaches HEALTH_VALUE 0 - It dies.

- A player is removed from the game, when they have no animals and no money left.

- Each animal can only eat between 1-3 types of food within the game. A player is not allowed to feed an Animal with a 
  food type that is not within it's given food types.
  
- After the last round, all animals are sold off and the Player receives currency equal to the value of their sold animals.

- The one player who has the most money at the end of the game, wins.

- At the start of each round, each player is presented with information about what
    animals they own, what food they own, how much money they own and how much decay
    their animals have suffered since their last round. [DONE]
  
- A player cannot sell food. [DONE]

# TO-DO LIST
- Buy an amount of food (As long as it's afforded) - Food is bought in kilos
    and the price is defined as per kilo. //Current implementation allows for Grams to be specified, will be updated
    
- Feed their animals (What kind of food is specified for each animal on an individual basis) //About 50% Done

- Attempt to breed **ONE** set of animals. There is a 50% chanse that a new animal will be
    made from doing this (This requires the same type of animal and different genders on each animal). 
      
      In case a baby animal is made, the player can name the baby/babies. Each respective
    baby has a 50:50 chanse of being Male or Female. //Not started
    
- I.e, 100 (OLD_VALUE) - 23 (DECAY) = 77 (NEW_HEALTH_VALUE). To replenish the
      HEALTH_VALUE - a player must feed their animals. //Not fully implemented, since eating is not fully integrated yet
      
- Each player gets to do actions from **ONE** of the categories above, then their turn is over. //Waiting for clarification on Corner case of insufficient resources

- If an animal reaches HEALTH_VALUE 0 - It dies. //Not started

- A player is removed from the game, when they have no animals and no money left. //Waiting for clarification on Corner case of having enough resources to not be eliminated but not sufficient to buy something

- Each animal can only eat between 1-3 types of food within the game. A player is not allowed to feed an Animal with a 
  food type that is not within it's given food types. //Eating is not fully implemented yet

- After the last round, all animals are sold off and the Player receives currency equal to the value of their sold animals. //Not started

- The one player who has the most money at the end of the game, wins. //Not started
