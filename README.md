# TimePays
A Minecraft mod tracking player online time and giving out rewards.

### License
http://fexcraft.net/license?id=mods

### Discord
https://discord.gg/AkMAzaA

### Usage
Example config:
```
[
	{
		"id":"test_item_0",
		"type": "item",
		"total": false,
		"interval-type": "s",
		"interval-time": 19,
		"one-time": true,
		"reward": {
			"item": "minecraft:emerald",
			"meta": 0, "count": 1,
			"message": "&7Your One time reward: &ban Emerald!"
		}
	},
	{
		"id":"test_item_1",
		"type": "item",
		"total": false,
		"interval-type": "s",
		"interval-time": 10,
		"one-time": false,
		"reward": {
			"item": "minecraft:stone",
			"meta": 5, "count": 1,
			"message": "&7Your timed reward: &ea Stone!"
		}
	},
	{
		"id":"test_fsmm_0",
		"type": "fsmm_item",
		"total": true,
		"interval-type": "s",
		"interval-time": 10,
		"one-time": true,
		"reward": {
			"amount": 50000,
			"message": "&6You received &a50$ &6as welcome!"
		}
	},
	{
		"id":"test_fsmm_1",
		"type": "fsmm_currency",
		"total": false,
		"interval-type": "s",
		"interval-time": 10,
		"one-time": false,
		"reward": {
			"amount": 10,
			"message": "&9You received &a0.01$ &9for being online this interval!"
		}
	}
]
```
Example result:
![img0](https://cdn.discordapp.com/attachments/365126848914391040/654005866621698071/unknown.png)
![img1](https://cdn.discordapp.com/attachments/365126848914391040/654005940013629470/unknown.png)

#### Config
The config has only one value - the interval - it is in **minutes**.

#### Values
- `id` - **unique** ID of the reward
- `type` - **type/handler** of the reward, currently available are:
  - `item` - to give items/blocks
  - `fsmm_item` - to give valid FSMM money items
  - `fsmm_currency` - to give FSMM money into the player's account
- `total` - if the reward is for total time played, or the current session (boolean, default `false`)
- `interval-type` - type value of interval, ca be `ms, s, m, h, d` respectively, by default `ms`
- `interval-time` - interval in which this reward is checked relative to the player
- `one-time` - if this reward has to be given only once (total or this session), by default `false`
- `reward` - JSON object with data for the reward handler (see bellow)

#### Handlers
As an important note, you can add new handlers too (e.g. ship with your mod)!    
Or contribute to the project's reward handlers.
- `item` - values for this handler are:
  - `item` - the item to be given, e.g. `minecraft:stone`
  - `meta` - metadata value of the item (or damage), **optional, default** `0`
  - `count` - count/amount of the to be given items
  - `nbt` - optional NBT data of the item - **in JSON form**
  - `message` - optional message to be sent to the player when receiving this reward
- `fsmm_item` - values for this handler are:
  - `amount` - value/amount of the items to be given, remember that in FSMM _1000 = 1$_!
  - `message` - optional message to be sent to the player when receiving this reward
- `fsmm_currency` - values for this handler are:
  - `amount` - amount that will be added to the players **account**, remember that in FSMM _1000 = 1$_!
  - `message` - optional message to be sent to the player when receiving this reward
