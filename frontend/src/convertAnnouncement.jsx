export default function convertAnnouncement(props) {
    let message;

    switch (props.eventID) {
        case "CORRECT_GUESS": message = "You guessed correctly!"
        break;
        case "ROUND_START": message = "Round " + props.message + " has started!";
        break;
        case "CURRENT_WORD": message = "You are drawing. The current word is: " + props.message;
        break;
        case "PLAYER_JOINED": message = "Player " + props.message + " has joined!";
        break;
        case "PLAYER_LEFT": message = "Player " + props.message + " has left!";
        break;
        case "CORRECT_GUESS_ANNOUNCEMENT": message = props.message + " has guessed correctly!";
        break;
        case "GAME_OVER": message = "Game is over!";
        break;
        case "CURRENT_DRAWER": message = "The current drawer is: " + props.message;
        break;
    }
    return message;
}