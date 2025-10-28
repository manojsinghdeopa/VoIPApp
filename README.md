# VoIPApp

This is a VoIP (Voice over Internet Protocol) application for Android.

## Features

*   **Real-time Communication:** Utilizes WebSockets for real-time, bidirectional communication between the app and the signaling server.
*   **Call Management:** Implements a robust call management system, including features to initiate, answer, reject, and hang up calls.
*   **Incoming Call Notifications:** Displays full-screen notifications for incoming calls, allowing users to accept or reject calls directly from the notification.
*   **Background Service:** Uses a foreground service (`CallService`) to manage calls, ensuring that calls continue even when the app is in the background.
*   **Clean Architecture:** Follows a clean architecture pattern, separating concerns into `ui`, `viewmodel`, `repository`, and `network` layers.
*   **Jetpack Compose:** The UI is built entirely with Jetpack Compose, a modern toolkit for building native Android UI.

## Architecture

The app follows a simplified clean architecture pattern:

*   **`ui`:** Contains the Composable functions that make up the app's UI.
*   **`viewmodel`:** Holds the `CallViewModel`, which is responsible for managing the state of the UI and handling user interactions.
*   **`network`:** Manages the WebSocket connection to the signaling server and handles sending and receiving of call-related messages.
*   **`service`:** Includes the `CallService` for managing calls in the foreground and the `CallReceiver` for handling notification actions.
*   **`model`:** Defines the data models used throughout the app, such as `CallState`.

## How it Works

1.  **WebSocket Connection:** The app establishes a WebSocket connection to a signaling server when the `CallViewModel` is initialized.
2.  **Registration:** The user can register with a user ID to the signaling server.
3.  **Initiating a Call:** A user can initiate a call to another user by providing their user ID.
4.  **Incoming Call:** When a call is initiated, the recipient receives an `incoming_call` message from the WebSocket server. The `CallViewModel` then starts the `CallService` to display a full-screen incoming call notification.
5.  **Answering/Rejecting:** The user can accept or reject the call from the notification. These actions are handled by the `CallReceiver`, which communicates the user's choice back to the `CallViewModel`.
6.  **Call State Management:** The `CallViewModel` manages the call state throughout the entire process, from `Idle` to `Calling`, `Incoming`, `Connected`, and `Ended`.

## Setup

1.  **Clone the repository.**
2.  **Update the server IP address:** In `NetworkUtils.kt`, change the `baseIp` to the IP address of your signaling server.
3.  **Run the app.**

## Permissions

The application requires the following permissions to function correctly:

*   `VIBRATE`: To vibrate the device for incoming calls.
*   `FOREGROUND_SERVICE`: To run the call service in the foreground.
*   `WAKE_LOCK`: To keep the device awake during a call.
*   `POST_NOTIFICATIONS`: To display call notifications.
*   `FOREGROUND_SERVICE_MEDIA_PLAYBACK`: To allow the foreground service to play media.
*   `INTERNET`: To connect to the internet for VoIP calls.
