# Pokedex

Since the goal was to create a Pokémon search app, I thought the most suiting name to be 'Pokedex'.

## Project structure
The project is separated into 4 different layers:
- Core
>- Application entry point
- Data
>- Data managers, 
>- Data converters
>- Data models
>- Types
>- Type extensions
- Domain
>- Utilities
>- Middle layer components invoked by the viewModels or controllers
- UI (with the MVVM pattern)
>- ViewModels for the business logic
>- Controllers and adapters to handle user inputs and present data)

**Note:** The project also has some unit tests.

## Communication between Controllers and ViewModels
- Controller->ViewModel 
>- Direct invocations
- ViewModel -> Controller
>- MutableLiveData observables for state updates (enabling/disabling buttons, updating UI data with state changes)
>- EventBus for requesting non-state updates related actions to the Controller (showing alerts, showing/hiding loading, in-app navigation)
**Note:** Each event has an `EventType` and a nullable `Map<String,Any>` payload.

### Typical communication flows
![image](https://user-images.githubusercontent.com/26168007/115551424-f55f5780-a2a2-11eb-8056-65daa589b1fc.png)
_____

![image](https://user-images.githubusercontent.com/26168007/115551467-07d99100-a2a3-11eb-9b97-536d929f60ed.png)


## Dependency Injection (DI)
I used Hilt for DI, so the components would access a a behaviour abstraction and not a concretion, for better flexibility of implementation (respecting one of the SOLID principles in the process).

**Note:** All pieces in the 'Domain' (except for 'Scope') are injected into the DI container.

## API access
I used [Retrofit](https://square.github.io/retrofit/) for accessing the Pokémon API and for POST(ing)  favourite Pokémons in the WebHook.

**Note:** WebHook url used [here](https://webhook.site./#!/c198ff0a-488a-4238-999e-36673d6e9654/f2d5da8d-476e-4e41-b56b-71f0fe06eb1c/1).

## Image loading
I used [Picasso](https://square.github.io/picasso/) for loading images from the Pokémon API and into the ImageViews.

## Animations
I used [Lottie](https://github.com/airbnb/lottie-android) for rendering animations.

## Persistence
Instead of using Room or ObjectBox for persistence, due to the simplicity of this exercise, I used SharedPreferences for persisting Pokemon details to save download time and network data.

## Data loading
I believe there were (at least) two possible solutions for data loading here:
1. Load all the Pokémons (in the search criteria, naturally) with their details when searching and, when navigating, we'd already have all the necessary data to render/use
2. Load the 'basic' Pokémon data when searching and, when navigating, we'd need to invoke the Pokémon API to gather the remaining data

I've used the first approach here. 
For this type of app, a 'once-only' loading time, even if considerable for greater searches, and future instant navigation would be preferable (in my opinion).

## Pagination and limits
I've implemented a pagination system to load and render the Pokémon data.
The user can select how many items per page he/she wants to see, from a predefined list (10, 25, 50 or 100).

## Pokémon details
The rendered Pokémon details are its sprites (using a `Carousel` supported by a `LinkedList`) and its stats (HP, Attack, ...).

**Note:** I've used polymorphism to access each Pokémon stat details, since each has its own resource, color and name.

## Search scenarios
There are three scenarios for accessing the Pokémon API:
- Empty search
> Accesses the default Pokémon API route with the user defined page limit and retrieves a `PokemonSearch` object, with the total Pokémons found, the next page's url and a `List<Pokemon>` 
- Page navigation
> Depending on whether it's forward or back navigation, it uses the `PokemonSearch` fields `next` or  `previous` urls as Pokémon API access point.
- Value search
> Accesses the Pokémon API using the search value in the url last path, and returns a `Pokemon` object.

## Miscellaneous tech details
The app is written entirely using [Kotlin](https://kotlinlang.org/) and I used Kotlin coroutines for thread management.
