package pl.pjatk.s16604.proj1.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_finish.*
import pl.pjatk.s16604.proj1.*
import pl.pjatk.s16604.proj1.models.Result
import java.time.LocalDateTime


class FinishActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    //initial default values in case of lack of SP
    private var cookies = 0L
    private var upgrades = initUpgrades()
    private var highscores = initHighscores()
    private var timer = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)
        loadData()
        onSaveClick()

        var sum = cookies
        upgrades.forEach {
            sum += it.calcPoints().toLong()
        }
        player_score.text = "Your score: $sum"

    }


    private fun onSaveClick() {
        saveBtn.setOnClickListener {
            val result = createResult()
            highscores.add(result)


            animate(this, saveBtn)
            saveData(highscores)
            goToScore()
        }
    }

    private fun createResult(): Result {
        val name = editText.text.toString()
        val date = LocalDateTime.now()

        var sum = 0L
        upgrades.forEach {
            sum += it.calcPoints().toLong()
        }

        val score = sum + cookies

        return Result(score, name, dateFormatter(date))

    }

    private fun goToScore() {
        finish()
        val intent = Intent(this, ScoreboardActivity::class.java)
        startActivity(intent)
    }

    private fun saveData(hs: MutableList<Result>) {
        STORAGE.saveHighScore(hs)
        STORAGE.resetData()

    }

    private fun loadData() {

        val metadata = STORAGE.loadData(this)

        sharedPreferences = metadata.sharedPreferences
        cookies = metadata.cookies
        upgrades = metadata.upgrades
        highscores = STORAGE.loadHighScore(this)
        timer = metadata.timer

    }
}
