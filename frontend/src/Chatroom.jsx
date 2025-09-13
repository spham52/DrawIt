import React from "react"
import './game.css'
import DrawCanvas from "./Canvas/DrawCanvas";

export default function Chatroom({messages, onSend, stompClientRef, gameID, playerID, drawerID, drawerName,
                                 currentWord}) {
    const [draft, setDraft] = React.useState("");

    const handleKeyDown = (event) => {
        if (event.key === "Enter") {
            event.preventDefault();
            onSend(draft);
            setDraft("");
        }
    }

    return (
        <div className="game-page">
            <div className="game-status">
                <p>Current Drawer: {drawerName}</p>
                <p>Current Word: {currentWord}</p>
            </div>
            <div className="draw-chat">
                <DrawCanvas stompClientRef={stompClientRef} playerID={playerID}
                            gameID={gameID} drawerID={drawerID}/>
                <div className="chatroom">
                    <ul className="message-list">
                        {messages.map((m, i) => (
                            <li key={i}>
                                {m.type === "chat" ? (
                                    <>
                                        <strong>{m.username}:</strong> {m.message}
                                    </>
                                ) : (
                                    <>
                                        {m.message}
                                    </>
                                )}
                            </li>
                        ))}
                    </ul>
                    <input className="chatbox"
                           type="text"
                           placeholder="Enter your guess here"
                           value={draft}
                           maxLength="50"
                           onChange={e => setDraft(e.target.value)}
                           onKeyDown={handleKeyDown}/>
                </div>
            </div>
        </div>
    )
}