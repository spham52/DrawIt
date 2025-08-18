import {Client} from '@stomp/stompjs'
import {useEffect, useRef, useState} from "react";
import Chatroom from "../Chatroom";

export function WebSocketChat(props) {
    const stompClientRef = useRef(null);
    const [messages, setMessage] = useState([]);

    const subscribeToGame = (gameID) => {
        if (!stompClientRef.current) return;

        stompClientRef.current.subscribe(`/topic/game/${gameID}`, (data) => {
            const serverResponse = JSON.parse(data.body);
            console.log(serverResponse);

            setMessage((prevMessage) => {
                const newMessages = [...prevMessage, serverResponse];
                return newMessages.length > 30
                    ? newMessages.slice(newMessages.length - 30)
                    : newMessages;
            })
        })

        stompClientRef.current.subscribe(`/topic/game/player/${sessionStorage.playerID}`, (data) => {
            const serverResponse = JSON.parse(data.body);
            console.log(serverResponse);
        })
    }

    const subscribeToGameEvent = (gameID, playerID) => {
        if (!stompClientRef.current) return;
        stompClientRef.current.subscribe(`/topic/game/${gameID}/event/player/${playerID}`, (data) => {
            const serverResponse = JSON.parse(data.body);
            console.log(serverResponse);
        })
        stompClientRef.current.subscribe(`/topic/game/${gameID}/event`, (data) => {
            const serverResponse = JSON.parse(data.body);
            console.log(serverResponse);
        })
    }

    useEffect(() => {
        console.log(props.name);
        const stompClient = new Client({
            brokerURL: "ws://localhost:8080/websocket",
            debug: (msg) => console.log("[STOMP]", msg),
        });

        stompClient.onConnect = (frame) => {
            console.log(frame);
            const sub = stompClient.subscribe('/topic/session', (data) => {
                const serverResponse = JSON.parse(data.body);
                console.log(serverResponse);
                sessionStorage.setItem("sessionID", serverResponse.sessionID);
                sessionStorage.setItem("gameID", serverResponse.gameID);
                sessionStorage.setItem("playerID", serverResponse.playerID);
                subscribeToGame(serverResponse.gameID);
                subscribeToGameEvent(serverResponse.gameID, serverResponse.playerID);
                sub.unsubscribe();
            })

            stompClient.publish({
                destination: "/app/connect",
                body: JSON.stringify({
                    'username': props.name,
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
            destination: "/app/chat",
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