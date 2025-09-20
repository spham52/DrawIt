import {useEffect, useRef, useState} from "react";
import './canvasStyle.css'

export default function DrawCanvas({stompClientRef, drawerID, playerID, gameID}) {
    const canvasRef = useRef(null);
    const subRef = useRef(null);
    const histSubRef = useRef(null);

    const sendDrawEvent = (coords) => {
        if (!stompClientRef.current?.connected || !gameID) return;

        stompClientRef.current.publish({
            destination: "/app/game/" + gameID + "/draw",
            body: JSON.stringify(coords)
        });
    }

    const subscribeToDrawEvent = (ctx) => {
        if (stompClientRef.current?.connected && gameID) {
            subRef.current = stompClientRef.current.subscribe("/topic/game/" + gameID + "/draw",
                (data) => {
                const coords = JSON.parse(data.body);
                ctx.beginPath();
                ctx.moveTo(coords.prevX, coords.prevY);
                ctx.lineTo(coords.x, coords.y);
                ctx.stroke();
            })
        }
    }


    const subscribeHistory = (ctx) => {
        histSubRef.current?.unsubscribe();
        if (stompClientRef.current?.connected && gameID) {
            histSubRef.current = stompClientRef.current.subscribe(
                `/user/queue/draw`,
                (data) => {
                    const coords = JSON.parse(data.body);
                    console.log(coords);
                    for (let i = 0; i < coords.length; i++) {
                        ctx.beginPath();
                        ctx.moveTo(coords[i].prevX, coords[i].prevY);
                        ctx.lineTo(coords[i].x, coords[i].y);
                        ctx.stroke();
                    }
                }
            );

            stompClientRef.current.publish({
                destination: `/app/game/${gameID}/requestHistory`,
                body: "{}"
            });
        }
    };



    useEffect(() => {
        let userDrawing = false;
        const canvas = canvasRef.current;

        if (!canvas) return;

        const ctx = canvas.getContext('2d');
        const dpr = window.devicePixelRatio || 1;
        const rect = canvas.getBoundingClientRect();
        canvas.width = rect.width * dpr;
        canvas.height = rect.height * dpr;
        let prevX, prevY;

        if (stompClientRef.current?.connected) {
            subRef.current?.unsubscribe();
            subscribeToDrawEvent(ctx);
            subscribeHistory(ctx);
        }

        const isDrawing = (event) => {
            const rect = canvas.getBoundingClientRect();
            prevX = (event.clientX - rect.left) * dpr;
            prevY = (event.clientY - rect.top) * dpr;
            userDrawing = true;
        }

        const stopDrawing = () => {
            userDrawing = false;
        }

        const drawFunc = (event) => {
            if (!userDrawing) return;
            const rect = canvas.getBoundingClientRect();
            const x = (event.clientX - rect.left) * dpr;
            const y = (event.clientY - rect.top) * dpr;
            if (playerID === drawerID) {
                // publish user's draw coords to websocket channel 'game/draw'
                sendDrawEvent({
                    prevX: prevX,
                    prevY: prevY,
                    x: x,
                    y: y
                });

                prevX = x;
                prevY = y;
            }
        }

        canvas.addEventListener('mousedown', isDrawing);
        canvas.addEventListener('mouseup', stopDrawing);
        canvas.addEventListener('mousemove', drawFunc)

        return () => {
            canvas.removeEventListener('mousedown', isDrawing);
            canvas.removeEventListener('mouseup', stopDrawing);
            canvas.removeEventListener('mousemove', drawFunc);
            subRef.current?.unsubscribe();
            subRef.current = null;
        }
    }, [drawerID, playerID, gameID, stompClientRef.current?.connected]);
    return <canvas ref={canvasRef} className="drawBoard"></canvas>
}