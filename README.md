# Fibonacci-Generator
An Android app to generate Fibonacci sequence values while using Kotlin, Hilt, Room and Compose
Dark Mode |  Light Mode
:-------------------------:|:-------------------------:
![Screenshot_20230205_012733_Fibonacci](https://user-images.githubusercontent.com/39101307/216842154-9fe9e62d-bedc-492c-b392-f18c56fe4358.jpg)   |  ![Screenshot_20230205_193132_Fibonacci](https://user-images.githubusercontent.com/39101307/216842168-6505e9e5-9d8e-4bac-ab40-ac102e02eda9.jpg)

## Components
In this project the idea is to generate a value from the Fibonacci sequence using a random order number.
It was assumed an interval of 0 to 250 for the order number but the performance of the app can be tested with other limits, like 10000.

The architecture is based in Clean Code and uses MVVM, Repository pattern, Observer pattern and others
As a dependency injection solution it was used Hilt.

In terms of calculations it was chosen a recursive approach, that first checks if the value is not stored (persisted) in the Database, in order to reduce the number of required operations, leading to a good performance. Each time a new order value is calculated it is added to the Database.
Also in terms of data to show in the UI, each time a new Fibonacci value is request it will be added to the Database including the time of the operation. If the storing opearation fails it will be returned a proper exception that translates into feedback for the user.

When it comes to the UI the decision was to use Jetpack Compose and it consists of 4 elements: add button, values list, empty view and loading view.
The values list shows every requested Fibonacci value, including order number and time of operation. The latest requested item is designed different than the remainder and will always be shown at the top of the list.

There are unit tests to test bothe the correcteness of the calculation algorith and the logic in the Repository class

## Step by step
- Open the fresh installed app and sees the empty view
- Click on the add button
- Loading view is shown and a new Fibonacci calculation is requested
- If calculation result is successful a new list item is added and empty view is gone
- If an error happens it is shown a Sanckbar with a message
- Closing the app and coming back to it will show all previous requested values

## Logic
The UI will observe the Database table where the requested Fibonacci values are store, making the list reactive to any change.
Each time a new Fibonacci value is requested, it will be calculated (if it wasn't already calculated previously) and added to the database, which will trigger an UI update, adding the new value a the latest one in the list.
