import {Client} from '@stomp/stompjs'
import {useEffect, useRef, useState} from "react";
import Chatroom from "../Chatroom";
import convertAnnouncement from "../convertAnnouncement";

export function WebSocketChat(props) {
    const stompClientRef = useRef(null);
    const [messages, setMessage] = useState([]);
    const [gameID, setGameID] = useState(null);
    const [playerID, setPlayerID] = useState(null);
    const [sessionID, setSessionID] = useState(null);
    const [drawerID, setDrawerID] = useState(null);
    const [drawerName, setDrawerName] = useState(null);
    const [currentWord, setCurrentWord] = useState(null);

    const subscribeToGame = (gameID, playerID) => {
        if (!stompClientRef.current) return;

        stompClientRef.current.subscribe(`/topic/game/${gameID}`, (data) => {
            const serverResponse = JSON.parse(data.body);
            console.log(serverResponse);

            messageFunc(serverResponse, "chat");
        })

        stompClientRef.current.subscribe(`/topic/game/player/${playerID}`, (data) => {
            const serverResponse = JSON.parse(data.body);
            console.log(serverResponse);
        })
    }

    const messageFunc = (serverResponse, type) => {
        const message = type === "chat" ?
            { type: "chat", username: serverResponse.username, message: serverResponse.message }
            : {type: "announcement", message: convertAnnouncement(serverResponse)};
        setMessage((prevMessage) => {
            const newMessages = [...prevMessage, message];
            return newMessages.length > 30
                ? newMessages.slice(newMessages.length - 30)
                : newMessages;
        });
    }

    const subscribeToGameEvent = (gameID, playerID) => {
        if (!stompClientRef.current) return;
        stompClientRef.current.subscribe(`/topic/game/${gameID}/event/player/${playerID}`, (data) => {
            const serverResponse = JSON.parse(data.body);
            messageFunc(serverResponse, "announcement");

            if (serverResponse.eventID === 'CURRENT_WORD') {
                setCurrentWord(serverResponse.message);
            }
        })
        stompClientRef.current.subscribe(`/topic/game/${gameID}/event`, (data) => {
            const serverResponse = JSON.parse(data.body);
            console.log(serverResponse);

            if (serverResponse.eventID === 'CURRENT_DRAWER') {
                setDrawerID(serverResponse.drawerID);
                setDrawerName(serverResponse.message);
            }

            if (drawerID !== playerID) {
                setCurrentWord("");
            }

            messageFunc(serverResponse, "announcement");
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
                setPlayerID(serverResponse.playerID);
                setGameID(serverResponse.gameID);
                setSessionID(serverResponse.sessionID);

                subscribeToGame(serverResponse.gameID, serverResponse.playerID);
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
                'playerID': playerID,
                'gameID': gameID,
                'message': message
            })
        })
    }

    return (
        <Chatroom messages={messages}
                  onSend={sendMessage} stompClientRef={stompClientRef} drawerID={drawerID} playerID={playerID}
                  gameID={gameID} drawerName={drawerName} currentWord={currentWord}
        />
    )

}