import React from "react"
import './game.css'

export default function Chatroom({messages, onSend}) {
    const [draft, setDraft] = React.useState("");

    const handleKeyDown = (event) => {
        if (event.key === "Enter") {
            event.preventDefault();
            onSend(draft);
            setDraft("");
            console.log(sessionStorage.getItem("playerID"))
            console.log(sessionStorage.getItem("gameID"))
        }
    }

    return (
        <div className="game-page">
            <div className="drawBoard"/>
            <div className="chatroom">
                <ul className="message-list">
                    {messages.map((m, i) => (
                        <li key={i}>
                            <strong>{m.playerID}:</strong> {m.message}
                        </li>
                    ))}
                </ul>
                <input className="chatbox"
                       type="text"
                       placeholder="Enter your guess here"
                       value={draft}
                       onChange={e => setDraft(e.target.value)}
                       onKeyDown={handleKeyDown}/>
            </div>
        </div>
    )
}