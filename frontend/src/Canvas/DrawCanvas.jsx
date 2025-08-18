import {useEffect, useRef, useState} from "react";
import './canvasStyle.css'

export default function DrawCanvas() {
    const canvasRef = useRef(null);

    useEffect(() => {
        let userDrawing = false;
        const canvas = canvasRef.current;
        const ctx = canvas.getContext('2d');
        const rect = canvas.getBoundingClientRect();
        const dpr = window.devicePixelRatio || 1;
        canvas.width = rect.width;
        canvas.height = rect.height;
        let prevX, prevY;

        const isDrawing = (event) => {
            prevX = (event.clientX - rect.left) * dpr;
            prevY = (event.clientY - rect.top) * dpr;
            userDrawing = true;
        }

        const stopDrawing = () => {
            userDrawing = false;
        }

        const drawFunc = (event) => {
            let x = (event.clientX - rect.left) * dpr;
            let y = (event.clientY - rect.top) * dpr;
            ctx.fillStyle = 'black';
            ctx.strokeWidth = '1px';

            if (userDrawing) {
                console.log("hi");
                ctx.beginPath();
                ctx.moveTo(prevX, prevY);
                ctx.lineTo(x, y);
                ctx.stroke();
                prevX = x;
                prevY = y;
            }
        }


        canvas.addEventListener('mousedown', isDrawing);
        canvas.addEventListener('mouseup', stopDrawing);
        canvas.addEventListener('mousemove', drawFunc)
    }, [])
    return <canvas ref={canvasRef} className="canvas"></canvas>
}