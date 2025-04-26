import React from 'react'
import Navbar from "./Components/Navbar";

export default function Home() {

    return (
        <div className="home">
            <Navbar/>
            <section className="main-home-section">
                <div className="inputBoxes">
                    <input type="text" name="name" placeholder="Enter your name"></input>
                    <input type="text" name="inviteCode" placeholder="Enter an invite code (optional)"></input>
                    <input type="button" value="Play" className="playButton"/>
                    <input type="button" value="Create a private server" className="private-server-button"/>
                </div>
            </section>
        </div>
    )
}