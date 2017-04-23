package services

import com.google.inject.{Inject, Singleton}

/**
  * Created by jessechen on 4/22/17.
  */

@Singleton
class ClassifyData @Inject() (trainingDao: TrainingDao, redditAPI: RedditAPI, twitterAPI: TwitterAPI) {
  val placeHolder = false
}
