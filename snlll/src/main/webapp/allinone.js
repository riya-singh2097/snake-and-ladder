const boardSize = 10;
const board = document.getElementById('board');
const status = document.getElementById('status');
const playerButtons = document.querySelectorAll("[id^='player']");
const playerCoins = {};
const playerPositions = {};
let currentPlayer = 1;
let isRolling = false;
const numPlayers = playerButtons.length;

// Initialize players dynamically
for (let i = 1; i <= numPlayers; i++) {
    playerCoins[i] = document.getElementById(`coin${i}`);
    playerPositions[i] = 1;
}

// Snakes and Ladders positions
const snakesAndLadders = {
    27: 5, 40: 3, 43: 18, 54: 31, 66: 45, 76: 58, 89: 53, 99: 41,
    4: 25, 13: 46, 42: 63, 50: 69, 62: 81, 74: 92, 33: 49
};

// Dice face mappings
const diceFaces = {
    1: [4], 2: [0, 8], 3: [0, 4, 8], 4: [0, 2, 6, 8], 
    5: [0, 2, 4, 6, 8], 6: [0, 1, 2, 6, 7, 8]
};

// Render Dice UI
function renderDice(number) {
    const dice = document.getElementById('dice');
    dice.innerHTML = '';
    for (let i = 0; i < 9; i++) {
        const dotContainer = document.createElement('div');
        dotContainer.className = 'dot-container';
        if (diceFaces[number].includes(i)) {
            const dot = document.createElement('div');
            dot.className = 'dot';
            dotContainer.appendChild(dot);
        }
        dice.appendChild(dotContainer);
    }
}

// Roll Dice & Move Player
function rollDice(player) {
    if (player !== currentPlayer || isRolling) {
        status.innerText = `It's Player ${currentPlayer}'s turn!`;
        return;
    }

    isRolling = true;
    const dice = document.getElementById('dice');
    dice.classList.add('rolling');

    setTimeout(() => {
        const result = Math.floor(Math.random() * 6) + 1;
        renderDice(result);
        dice.classList.remove('rolling');

        let oldPosition = playerPositions[player];
        let newPosition = oldPosition + result;
        if (newPosition > boardSize * boardSize) {
            newPosition = oldPosition;
        }

        const finalPosition = snakesAndLadders[newPosition] || newPosition;
        if (snakesAndLadders[newPosition]) {
            const type = finalPosition > newPosition ? 'ladder' : 'snake';
            status.innerText = `Player ${player} rolled ${result}, moved to ${newPosition}, and took a ${type} to ${finalPosition}.`;
        } else {
            status.innerText = `Player ${player} rolled ${result} and moved to position ${finalPosition}.`;
        }

        moveCoin(player, finalPosition);
        playerPositions[player] = finalPosition;

        if (finalPosition === 100) {
            status.innerText = `Player ${player} wins! Game over.`;
            alert(`Player ${player} wins! Game over.`);
            disableAllButtons();
            return;
        }

        currentPlayer = currentPlayer === numPlayers ? 1 : currentPlayer + 1;
        status.innerText += ` Player ${currentPlayer}'s turn.`;

		// Store move in database
		fetch('PlayerMoveServlet', {
		    method: 'POST',
		    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		    body: `playerId=${player}&diceRoll=${result}&oldPosition=${oldPosition}&newPosition=${finalPosition}`
		})
		.then(response => response.text())
		.then(data => console.log("Server Response:", data))
		.catch(error => console.error("Error sending move data:", error));
		
        isRolling = false;
    }, 500);
}

// Move Coin Function
function moveCoin(player, newPosition) {
    const coin = playerCoins[player];
    const row = Math.floor((newPosition - 1) / boardSize);
    const col = (newPosition - 1) % boardSize;
    const x = (row % 2 === 0) ? col : boardSize - 1 - col;
    const y = boardSize - 1 - row;
    coin.style.transform = `translate(${x * 42}px, ${y * 40}px)`;
}

// Disable All Buttons on Win
function disableAllButtons() {
    playerButtons.forEach(button => button.disabled = true);
}

// Enable Buttons on Game Start
function enableAllButtons() {
    playerButtons.forEach(button => button.disabled = false);
}

// Add Event Listeners for Dice Rolls
playerButtons.forEach((button, index) => {
    button.addEventListener('click', () => rollDice(index + 1));
});
//start game
document.getElementById('startGameBtn').addEventListener('click', () => {
    fetch('StartGameServlet', { method: 'POST' }) // This sets the new game ID in the session
        .then(response => response.text())
        .then(data => {
            console.log("Server Response:", data);
            enableAllButtons();
            status.innerText = "Game started! Player 1's turn.";
            for (let i = 1; i <= numPlayers; i++) {
                playerPositions[i] = 1;
                moveCoin(i, 1);
            }
            currentPlayer = 1;
            renderDice(1);
        })
        .catch(error => console.error("Error starting game:", error));
});


// Exit Game
document.getElementById('exitBtn').addEventListener('click', () => {
    location.href = "index.html";
});

// Initialize Dice on Page Load
document.addEventListener('DOMContentLoaded', () => {
    disableAllButtons();
    renderDice(1);
});
