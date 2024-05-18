let stompClient = null;
let game = null;
let player = null;

/**
 * Sends a message to the server using the STOMP client.
 * @param {Object} message - The message to be sent. Must contain at least a "type" field.
 */
const sendMessage = (message) => {
    stompClient.send(`/app/${message.type}`, {}, JSON.stringify(message));
}

/**
 * Sends a move message to the server.
 * @param {Number} move - The index of the cell where the move should be made.
 */
const makeMove = (move) => {
    sendMessage({
        type: "game.move",
        move: move,
        turn: game.turn,
        sender: player,
        gameId: game.gameId
    });
}

/**
 * An object containing functions to handle each type of message received from the server.
 */
let subscribed = false;
const messagesTypes = {
    "game.join": (message) => {
        updateGame(message);
    },
    "game.gameOver": (message) => {
        updateGame(message);
        showWinner(message.winner);
    },
    "game.joined": (message) => {
        if (game !== null && game.gameId !== message.gameId) return;
        player = localStorage.getItem("playerName");
        updateGame(message);

        if (!subscribed) {
            const socket = new SockJS('/ws');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, function (frame) {
                stompClient.subscribe(`/topic/game.${message.gameId}.${message.sender}`, function (message) {
                    handleMessage(JSON.parse(message.body));
                });
            });
            console.log(`/topic/game.${message.gameId}.${message.sender}`);
            subscribed = true;
        }
        
    },
    "game.move": (message) => {
        updateGame(message);
    },
    "game.left": (message) => {
        updateGame(message);
        if (message.winner) showWinner(message.winner);
    },
    "error": (message) => {
        toastr.error(message.content);
    }
}

/**
 * Handles a message received from the server.
 * @param {Object} message - The message received.
 */
const handleMessage = (message) => {
    if (messagesTypes[message.type])
        messagesTypes[message.type](message);
}

/**
 * Converts a message received from the server into a game object.
 * @param {Object} message - The message received.
 * @returns {Object} The game object.
 */
const messageToGame = (message) => {
    return {
        gameId: message.gameId,
        board: message.board,
        enemyBoard: message.enemyBoard,
        turn: message.turn,
        player1: message.player1,
        player2: message.player2,
        gameState: message.gameState,
        winner: message.winner,
        leaderBoard: message.leaderBoard,
        matchHistory: message.matchHistory
    }
}

/**
 * Displays a success message with the name of the winning player.
 * @param {String} winner - The name of the winning player.
 */
const showWinner = (winner) => {
    toastr.success(`The winner is ${winner}!`);
}

/**
 * Starts the process of joining a game. Asks the player to enter their name and sends a message to the server requesting to join the game.
 */
const joinGame = () => {
    const playerName = prompt("Enter your name:");
    localStorage.setItem("playerName", playerName);
    sendMessage({
        type: "game.join",
        player: playerName
    });
}

/**
 * Connects the STOMP client to the server and subscribes to the "/topic/game.state" topic.
 */
const connect = () => {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/game.state', function (message) {
            handleMessage(JSON.parse(message.body));
        });
        loadGame();
    });
}

/**
 * Attempts to load a game by joining with the player's previously stored name, or prompts the player to enter their name if no name is stored.
 */
const loadGame = () => {
    const playerName = localStorage.getItem("playerName");
    if (playerName) {
        sendMessage({
            type: "game.join",
            player: playerName
        });
    } else {
        joinGame();
    }
}

/**
 * Updates the game state with the information received from the server.
 * @param {Object} message - The message received from the server.
 */
const updateGame = (message) => {
    game = messageToGame(message);
    updateBoard(message.board, document.getElementById('board'));
    updateBoard(message.enemyBoard, document.getElementById('enemy'));
    updateLeaderBoard(message.leaderBoard);
    updateMatchHistory(message.matchHistory);
    document.getElementById("player1").innerHTML = game.player1;
    document.getElementById("player2").innerHTML = game.player2 || (game.winner ? '-' : 'Waiting for player 2...');
    document.getElementById("turn").innerHTML = game.turn;
    document.getElementById("winner").innerHTML = game.winner || '-';
}

const updateLeaderBoard = (leaderBoard) => {
    let board = document.getElementById('Leaderboard');
    board.innerHTML = '';
    
    leaderBoard.forEach(player => {
        let listItem = document.createElement('li');
        listItem.textContent = player;
        board.appendChild(listItem);
    });
}

const updateMatchHistory = (matchHistory) => {
    let board = document.getElementById('Match History');
    board.innerHTML = '';

    matchHistory.forEach(player => {
        let listItem = document.createElement('li');
        listItem.textContent = player;
        board.appendChild(listItem);
    });
}

/**
 * Updates the game board with the information received from the server.
 * @param {Array} board - The board received from the server.
 */
const updateBoard = (board, boardElement) => {
    let counter = 0;
    board.forEach((row, rowIndex) => {
        row.forEach((cell, cellIndex) => {
            const cellElement = boardElement.querySelector(`.row-${rowIndex} .cell-${cellIndex}`);
            cellElement.innerHTML = cell === ' ' ? '<button onclick="makeMove(' + counter + ')"> </button>' : `<span class="cell-item">${cell}</span>`;
            counter++;
        });
    });
}


window.onload = function () {
    connect();
}