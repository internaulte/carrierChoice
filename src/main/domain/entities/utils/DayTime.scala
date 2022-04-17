package domain.entities.utils

import domain.entities.utils.Types.{Hour, Minute}

final case class DayTime(hours: Hour, minutes: Minute) {
  def isStrictBefore(otherDayTime: DayTime): Boolean = {
    this.hours < otherDayTime.hours || (this.hours == otherDayTime.hours && this.minutes < otherDayTime.minutes)
  }
}
