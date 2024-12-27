package com.example.tic_tac_toe

import android.app.AlertDialog
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tic_tac_toe.databinding.ActivityMainBinding
import com.example.tic_tac_toe.ui.theme.TictactoeTheme
import java.util.logging.Handler

class MainActivity : AppCompatActivity() {

    enum class Turn
    {
        NOUGHT,
        CROSS
    }
    enum class DifficultyLevel{
        Easy,
        Harder,
        Expert

    }
    private val handler = android.os.Handler()
    val difficulties =arrayOf("easy","harder","expert")
    private var dificultLevel = DifficultyLevel.Harder

    fun setDificultLevel(dificultLevel: DifficultyLevel){
        this.dificultLevel = dificultLevel
    }
    fun getDificultLevel():DifficultyLevel{
        return dificultLevel
    }
    private var firstTurn = Turn.CROSS
    private var currentTurn = Turn.CROSS

    private var crossesScore = 0
    private var noughtsScore = 0

    private lateinit var binding: ActivityMainBinding

    private var boardList = mutableListOf<Button>()
    private val imageViews = mutableListOf<ImageView>()
    private var savedBoardStates  = ArrayList<String>()

    private lateinit var playerSound: MediaPlayer // Sonido del jugador
    private lateinit var machineSound: MediaPlayer // Sonido de la máquina

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        //setSupportActionBar(toolbar)

        playerSound = MediaPlayer.create(this, R.raw.choque)
        machineSound = MediaPlayer.create(this, R.raw.roar)

        //playerSound.setOnCompletionListener {
        //    playerSound.release()
        //}
        //machineSound.setOnCompletionListener {
        //    machineSound.release()
        //}

        initBoard()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu_item, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.new_game -> {
                Toast.makeText(this, "Juego Nuevo", Toast.LENGTH_SHORT).show()
                resetBoard()
                return true
            }
            R.id.ai_difficulty -> {
                val singleChoiceDialog= AlertDialog.Builder(this)
                    .setTitle("Choose difficulty level")
                    .setSingleChoiceItems(difficulties,dificultLevel.ordinal) { dialog, which ->
                        val chosedDifficultyLevel=DifficultyLevel.values()[which]

                        setDificultLevel(chosedDifficultyLevel)
                        resetBoard()

                        Toast.makeText(
                            this,
                            "You choose ${difficulties[which]}",
                            Toast.LENGTH_SHORT
                        ).show()
                        dialog.dismiss()
                    }
                    .create()
                singleChoiceDialog.show()

                return true
            }
            R.id.quit -> {
                val simpleDialog = AlertDialog.Builder(this)
                    .setTitle("Quit")
                    .setMessage("Deseas salir?")
                    .setPositiveButton("SALIR"){
                            dialog, which ->
                        finish()
                    }
                    .setNegativeButton("CANCELAR"){
                            dialog, which ->
                    }
                    .create()
                simpleDialog.show()
                return true
            }
            R.id.info -> {
                val customView = layoutInflater.inflate(R.layout.dialog_custom, null)
                val dialogMessage = customView.findViewById<TextView>(R.id.dialog_message)
                val dialogImage = customView.findViewById<ImageView>(R.id.dialog_image)
                dialogImage.setImageResource(R.drawable.icon)

                val simpleDialog = AlertDialog.Builder(this)
                    .setTitle("Info")
                    .setView(customView)
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                simpleDialog.show()
                return true


            }


            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        savedBoardStates.clear()
        for (i in boardList.indices) {
            val symbol = boardList[i].text.toString().ifEmpty { "null" }
            savedBoardStates.add(symbol)
        }
        Log.d("TicTacToe", "Saved board states: $savedBoardStates")
        outState.putStringArrayList("savedBoardStates", savedBoardStates)
        outState.putInt("difficultyLevel", DifficultyLevel.values().indexOf(dificultLevel))
        Log.d("TicTacToe", "Saved board states: $savedBoardStates") // Log the saved states

    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        // Restore the board state
        val restoredStates = savedInstanceState.getStringArrayList("savedBoardStates") ?: ArrayList()

        //for (i in boardList.indices) {
        //    boardList[i].text = restoredStates.getOrNull(i) ?: ""
        //}
        Log.d("TicTacToe", "Restored board states: $restoredStates") // Log the restored states
        for (i in boardList.indices) {
            Log.d("TicTacToe","index: $i")
            //Log.d("TicTacToe","boardList: $boardList")
            val button = boardList[i]
            val symbol = if (restoredStates.getOrNull(i) == "null") null else restoredStates.getOrNull(i)
            Log.d("TicTacToe","symbol: $symbol")
            button.text = symbol


            when (symbol) {
                "X" -> imageViews[i].setImageResource(R.drawable.sword)
                "O" -> imageViews[i].setImageResource(R.drawable.dragon)
                else -> imageViews[i].setImageResource(R.drawable.puerta)
            }
        }
        val difficultyLevelOrdinal = savedInstanceState.getInt("difficultyLevel")
        setDificultLevel( DifficultyLevel.values()[difficultyLevelOrdinal])
        Log.d("TicTacToe", "Restored board states: $restoredStates") // Log the restored states
        // Update the UI based on the restored state
        updateBoardUI()
    }

    private fun updateBoardUI() {

    }



    private fun initBoard() {
        boardList.add(binding.one)
        boardList.add(binding.two)
        boardList.add(binding.three)
        boardList.add(binding.four)
        boardList.add(binding.five)
        boardList.add(binding.six)
        boardList.add(binding.seven)
        boardList.add(binding.eight)
        boardList.add(binding.nine)
        imageViews.add(binding.image1)
        imageViews.add(binding.image2)
        imageViews.add(binding.image3)
        imageViews.add(binding.image4)
        imageViews.add(binding.image5)
        imageViews.add(binding.image6)
        imageViews.add(binding.image7)
        imageViews.add(binding.image8)
        imageViews.add(binding.image9)
        for (button in boardList) {
            if (button.text.isEmpty()) {
                button.text = "" // Default to an empty string if not already set
            }
        }
        for(imageView in imageViews)
        {

            imageView.setImageResource(R.drawable.puerta)


        }


    }





    fun boardTapped(view: View) {
        if (view !is Button) return
        if (view.text != "") return // Check if the button is already occupied

        addToBoard(view)

        if (checkForVictory(NOUGHT)) {
            noughtsScore++
            result("Noughts Win!")
        } else if (checkForVictory(CROSS)) {
            crossesScore++
            result("Crosses Win!")
        } else if (fullBoard()) {
            result("Draw")
        } else {
            // Add computer's move
            val computerMoveIndex = getComputerMove(dificultLevel)

            handler.postDelayed({
            boardList[computerMoveIndex].text = NOUGHT
            imageViews[computerMoveIndex].setImageResource(R.drawable.dragon)
            machineSound.start()},1000)


            if (checkForVictory(NOUGHT)) {
                noughtsScore++
                result("Noughts Win!")
            } // Check for computer victory after its move
        }
    }
    private fun resetBoard()
    {
        for(button in boardList)
        {
            button.text = ""
        }
        firstTurn = Turn.CROSS
        currentTurn = firstTurn

        for(imageView in imageViews)
        {
            imageView.setImageResource(R.drawable.puerta)
        }

//        if(firstTurn == Turn.NOUGHT)
//            firstTurn = Turn.CROSS
//        else if(firstTurn == Turn.CROSS)
//            firstTurn = Turn.NOUGHT
//
//        currentTurn = firstTurn
//        setTurnLabel()
    }

    private fun fullBoard(): Boolean
    {
        for(button in boardList)
        {
            if(button.text == "")
                return false
        }
        return true
    }
    private fun addToBoard(button: Button)
    {
        val buttonIndex = boardList.indexOf(button)
        val imageView = imageViews[buttonIndex]
        if(button.text != "")
            return

//        if(currentTurn == Turn.NOUGHT)
//        {
//            //button.text = NOUGHT
//            //currentTurn = Turn.CROSS
//        }
         if(currentTurn == Turn.CROSS)
        {
            imageView.setImageResource(R.drawable.sword)
            button.text = CROSS

            playerSound.start()
            //currentTurn = Turn.NOUGHT
            //button.text = CROSS
            //currentTurn = Turn.NOUGHT
        }
        //setTurnLabel()
        seeCurrentTurn()
    }

    private fun seeCurrentTurn() {
        var turnText = ""
        if(currentTurn == Turn.CROSS)
            turnText = "Turn $CROSS"
        else if(currentTurn == Turn.NOUGHT)
            turnText = "Turn $NOUGHT"
        binding.turnTV.text = turnText
    }

    private fun setTurnLabel()
    {
        var turnText = ""
        if(currentTurn == Turn.CROSS)
            turnText = "Turn $CROSS"
        else if(currentTurn == Turn.NOUGHT)
            turnText = "Turn $NOUGHT"

        binding.turnTV.text = turnText
    }



    companion object
    {
        const val NOUGHT = "O"
        const val CROSS = "X"
    }
    private fun result(title: String)
    {
        //val message = "\nNoughts $noughtsScore\n\nCrosses $crossesScore"
        val message="Game over"
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Reset")
            { _,_ ->
                resetBoard()
            }
            .setCancelable(false)
            .show()
    }
    private fun checkForVictory(s: String): Boolean
    {
        //Horizontal Victory
        if(match(binding.one,s) && match(binding.two,s) && match(binding.three,s))
            return true
        if(match(binding.four,s) && match(binding.five,s) && match(binding.six,s))
            return true
        if(match(binding.seven,s) && match(binding.eight,s) && match(binding.nine,s))
            return true

        //Vertical Victory
        if(match(binding.one,s) && match(binding.four,s) && match(binding.seven,s))
            return true
        if(match(binding.two,s) && match(binding.five,s) && match(binding.eight,s))
            return true
        if(match(binding.three,s) && match(binding.six,s) && match(binding.nine,s))
            return true

        //Diagonal Victory
        if(match(binding.one,s) && match(binding.five,s) && match(binding.nine,s))
            return true
        if(match(binding.three,s) && match(binding.five,s) && match(binding.seven,s))
            return true

        return false
    }
    private fun match(button: Button, symbol : String): Boolean = button.text == symbol

    private fun getComputerMove(difficultyLevel: DifficultyLevel): Int {
        when (difficultyLevel) {
            DifficultyLevel.Easy -> {
                val availableMoves = boardList.indices.filter { boardList[it].text == "" }
                return availableMoves.random()
            }
            DifficultyLevel.Harder -> {
                // Check for winning move for computer (O)
                for (i in 0 until 9) {
                    if (boardList[i].text == "") {
                        val tempBoard = boardList.toMutableList()
                        tempBoard[i].text = NOUGHT
                        if (checkForVictory(NOUGHT)) {
                            return i
                        }
                        tempBoard[i].text = "" // Reset the temporary board
                    }
                }

                // If no winning move, choose a random available move
                val availableMoves = boardList.indices.filter { boardList[it].text == "" }
                return availableMoves.random()
            }
            DifficultyLevel.Expert -> {
                // Check for winning move for computer (O)
                for (i in 0 until 9) {
                    if (boardList[i].text == "") {
                        val tempBoard = boardList.toMutableList()
                        tempBoard[i].text = NOUGHT
                        if (checkForVictory(NOUGHT)) {
                            return i
                        }
                        tempBoard[i].text = "" // Reset the temporary board
                    }
                }

                // Check for blocking move against human (X)
                for (i in 0 until 9) {
                    if (boardList[i].text == "") {
                        val tempBoard = boardList.toMutableList()
                        tempBoard[i].text = CROSS
                        if (checkForVictory(CROSS)) {
                            return i
                        }
                        tempBoard[i].text = "" // Reset the temporary board
                    }
                }

                // If no winning or blocking moves, choose a random available move
                val availableMoves = boardList.indices.filter { boardList[it].text == "" }
                return availableMoves.random()
            }
        }
    }

}
