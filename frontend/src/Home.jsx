import React from 'react'
import Navbar from "./Components/Navbar";
import { WebSocketChat } from "./websocket/WebSocketChat";
import Chatroom from "./Chatroom";

export default function Home() {
    const [name, setName] = React.useState("");
    const [isInGame, setInGame] = React.useState(false);
    let nameInput = "";

    function handleSubmit(event) {
        event.preventDefault();
        const formEl = event.currentTarget;
        const formData = new FormData(formEl);
        nameInput = formData.get("name");
        setInGame(true);
    }

    return (
        <div className="home">
            <Navbar/>
            {!isInGame && <section className="main-home-section">
                <form className="inputBoxes" onSubmit={handleSubmit} method="post">
                    <input type="text" name="name" placeholder="Enter your name"></input>
                    <input type="text" name="inviteCode" placeholder="Enter an invite code (optional)"></input>
                    <input type="submit" value="Play" className="playButton"/>
                    <input type="button" value="Create a private server" className="private-server-button"/>
                </form>
            </section>}
            {isInGame && <WebSocketChat name={nameInput}/>}
        </div>
    )
}