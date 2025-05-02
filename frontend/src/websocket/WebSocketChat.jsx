import {Client} from '@stomp/stompjs'
import {useEffect, useRef, useState} from "react";
import Chatroom from "../Chatroom";

export function WebSocketChat(props) {
    const stompClientRef = useRef(null);
    const [messages, setMessage] = useState([]);
    useEffect(() => {
        console.log(props.name);
        const stompClient = new Client({
            brokerURL: "ws://localhost:8080/websocket"
        });

        stompClient.onConnect = (frame) => {
            console.log(frame);
            stompClient.subscribe('/topic/session', (data) => {
                const serverResponse = JSON.parse(data.body);
                console.log(serverResponse);
                sessionStorage.setItem("sessionID", serverResponse.sessionID);
                sessionStorage.setItem("gameID", serverResponse.gameID);
                sessionStorage.setItem("playerID", serverResponse.playerID);
            })

            stompClient.subscribe('/topic/chat', (data) => {
                const serverResponse = JSON.parse(data.body);
                setMessage((prevMessages) => [...prevMessages, serverResponse]);
                console.log(serverResponse);
            })

            stompClient.publish({
                destination: "/app/connect",
                body: JSON.stringify({
                    'name': props.name,
                    'inviteCode': '000'
                })
            })
        }
        stompClient.activate();
        stompClientRef.current = stompClient;
        return () => {
            stompClient.deactivate();
        }
    }, [props.name]);

    const sendMessage = (message) => {
        stompClientRef.current.publish({
            destination: "/app/chat/message",
            body: JSON.stringify({
                'playerID': sessionStorage.getItem("playerID"),
                'gameID': sessionStorage.getItem("gameID"),
                'message': message
            })
        })
    }

    return (
        <Chatroom messages={messages}
                  onSend={sendMessage}/>
    )

}