# LinearProgressBar
A Linear Progress bar with a text indicator with built-in animation.

![Screenshot 1](https://github.com/Special-N9NE/LinearProgressBar/blob/master/sample.gif)


## Usage 

```XML
<org.nine.linearprogressbar.LinearProgressBar
    android:id="@+id/pb"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_gravity="center"
    android:layout_margin="16dp"

    app:animationDuration="1000"
    app:font="@font/iran_sans_fa_num"
    app:gradient="true"
    app:gradientEndColor="#FFC107"
    app:gradientStartColor="#2196F3"
    app:progressBackgroundColor="#9F9595"
    app:progressColor="@color/design_default_color_secondary"
    app:progressValue="50"
    app:radius="10dp"
    app:textColor="@color/black"
    app:textSize="16sp"
    app:textVisibility="visible"
    app:thickness="8dp"
/>
```

Also there is a View called 'LinearVerticalProgressBar' which you can use it if you want a vertical ProgressBar


## Installation

1- Download [this aar file](https://raw.githubusercontent.com/Special-N9NE/LinearProgressBar/master/LinearProgressBar.aar)

2- Go to File -> Project Structure -> Dependencies

3- Click the plus icon to add a new dependency and select "JAR/AAR Dependency"

4- Select your preferred module

5- Paste the downloaded file location and now you can use this library


Enjoy :heart:
