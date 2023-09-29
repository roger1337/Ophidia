# 🐍 Ophidia

Ophidia is a Minecraft pathfinder with the goal of emulating realistic player movement, despite movement speed.

## How can I use this?

Create a 1.8.9 Forge project and copy the code provided. Inside Minecraft, run the /travel [x] [y] [z] command.

## Demonstration

![2023-09-29 10-13-57](https://github.com/roger1337/Ophidia/assets/85001442/24af5894-2ba8-4a66-9ebc-70adebfe393c)


## Technical

Ophidia uses A* along with string-pulling optimisations, finding valid paths across nodes computed by the A* algorithm.

The license is intentionally unrestricting so you may use this freely in projects where you would find Ophidia useful. All I ask for in return is a star 🙂.

It is built in forge for ease of testing, but can be easily repurposed to work with server plugins for uses such as PVP bots and NPC movement.

> Please don't use this in multiplayer scenarios for the purpose of cheating. It is also detectable in some ways I won't reveal and can get you banned.

