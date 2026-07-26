package com.team_daytodo.daytodo.domain.magazine.model

class MagazinePlaceNotFoundException(
    placeId: String,
) : NoSuchElementException("매거진 장소를 찾을 수 없어요. placeId=$placeId")
