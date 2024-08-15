package com.intellect.logos.domain.usecase.volume

import com.intellect.logos.domain.model.Asset
import com.intellect.logos.domain.model.exchange.Key
import com.intellect.logos.domain.model.exchange.Volume
import com.intellect.logos.domain.repository.ExchangeRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

class UpdateVolumeUseCaseTest : BehaviorSpec({

    val exchangeRepository: ExchangeRepository = mockk()
    lateinit var updateVolumeUseCase: UpdateVolumeUseCase

    val btcAsset = Asset(
        name = "BTC",
        description = "Bitcoin",
        icon = "icon"
    )

    beforeEach {
        coEvery { exchangeRepository.cacheVolume(any()) } returns Unit

        updateVolumeUseCase = UpdateVolumeUseCase(
            exchangeRepository = exchangeRepository
        )
    }

    context("UpdateVolumeUseCase") {
        `when`("invoke") {
            and("with backspace key") {
                and("volume is not empty") {
                    and("has single digit") {
                        then("should become zero") {
                            updateVolumeUseCase(
                                asset = btcAsset,
                                volume = Volume(
                                    value = 1.0,
                                    text = "1"
                                ),
                                key = Key.Backspace
                            )

                            coVerify {
                                exchangeRepository.cacheVolume(
                                    Volume(
                                        value = 0.0,
                                        text = "0"
                                    )
                                )
                            }
                        }
                    }
                    and("has multiple digits") {
                        then("should remove last character from volume") {
                            updateVolumeUseCase(
                                asset = btcAsset,
                                volume = Volume(
                                    value = 123.45,
                                    text = "123.45"
                                ),
                                key = Key.Backspace
                            )

                            coVerify {
                                exchangeRepository.cacheVolume(
                                    Volume(
                                        value = 123.4,
                                        text = "123.4"
                                    )
                                )
                            }
                        }
                    }
                }
                and("volume is zero") {
                    and("without dot") {
                        then("should remain zero") {
                            updateVolumeUseCase(
                                asset = btcAsset,
                                volume = Volume(
                                    value = 0.0,
                                    text = "0"
                                ),
                                key = Key.Backspace
                            )

                            coVerify {
                                exchangeRepository.cacheVolume(
                                    Volume(
                                        value = 0.0,
                                        text = "0"
                                    )
                                )
                            }
                        }
                    }
                    and("with dot") {
                        then("should become zero") {
                            updateVolumeUseCase(
                                asset = btcAsset,
                                volume = Volume(
                                    value = 0.0,
                                    text = "0."
                                ),
                                key = Key.Backspace
                            )

                            coVerify {
                                exchangeRepository.cacheVolume(
                                    Volume(
                                        value = 0.0,
                                        text = "0"
                                    )
                                )
                            }
                        }
                    }
                }
            }
            and("with number key") {
                and("decimal of volume is 2") {
                    then("should not append number to volume") {
                        updateVolumeUseCase(
                            asset = btcAsset,
                            volume = Volume(
                                value = 123.45,
                                text = "123.45"
                            ),
                            key = Key.Number(6)
                        )

                        coVerify {
                            exchangeRepository.cacheVolume(
                                Volume(
                                    value = 123.45,
                                    text = "123.45"
                                )
                            )
                        }
                    }
                }
                and("decimal of volume is less than 2") {
                    then("should append number to volume") {
                        updateVolumeUseCase(
                            asset = btcAsset,
                            volume = Volume(
                                value = 123.4,
                                text = "123.4"
                            ),
                            key = Key.Number(5)
                        )

                        coVerify {
                            exchangeRepository.cacheVolume(
                                Volume(
                                    value = 123.45,
                                    text = "123.45"
                                )
                            )
                        }
                    }
                }
                and("volume is zero") {
                    and("number is not zero") {
                        then("should replace zero with the number") {
                            updateVolumeUseCase(
                                asset = btcAsset,
                                volume = Volume(
                                    value = 0.0,
                                    text = "0"
                                ),
                                key = Key.Number(5)
                            )

                            coVerify {
                                exchangeRepository.cacheVolume(
                                    Volume(
                                        value = 5.0,
                                        text = "5"
                                    )
                                )
                            }
                        }
                    }

                    and("number is zero") {
                        then("should remain zero") {
                            updateVolumeUseCase(
                                asset = btcAsset,
                                volume = Volume(
                                    value = 0.0,
                                    text = "0"
                                ),
                                key = Key.Number(0)
                            )

                            coVerify {
                                exchangeRepository.cacheVolume(
                                    Volume(
                                        value = 0.0,
                                        text = "0"
                                    )
                                )
                            }
                        }
                    }
                }
                and("volume has max length") {
                    then("should not append number to volume") {
                        updateVolumeUseCase(
                            asset = btcAsset,
                            volume = Volume(
                                value = 1234567890.0,
                                text = "1,234,567,890"
                            ),
                            key = Key.Number(1)
                        )

                        coVerify {
                            exchangeRepository.cacheVolume(
                                Volume(
                                    value = 1234567890.0,
                                    text = "1,234,567,890"
                                )
                            )
                        }
                    }
                }
            }
            and("with dot key") {
                and("volume contains dot") {
                    then("should not append dot to volume") {
                        updateVolumeUseCase(
                            asset = btcAsset,
                            volume = Volume(
                                value = 123.4,
                                text = "123.4"
                            ),
                            key = Key.Dot
                        )

                        coVerify {
                            exchangeRepository.cacheVolume(
                                Volume(
                                    value = 123.4,
                                    text = "123.4"
                                )
                            )
                        }
                    }
                }
                and("asset is JPY") {
                    then("should not append dot to volume") {
                        updateVolumeUseCase(
                            asset = Asset(
                                name = "JPY",
                                description = "Japanese Yen",
                                icon = "icon"
                            ),
                            volume = Volume(
                                value = 123.4,
                                text = "123.4"
                            ),
                            key = Key.Dot
                        )

                        coVerify {
                            exchangeRepository.cacheVolume(
                                Volume(
                                    value = 123.4,
                                    text = "123.4"
                                )
                            )
                        }
                    }
                }
                and("volume is empty") {
                    then("should append 0. to volume") {
                        updateVolumeUseCase(
                            asset = btcAsset,
                            volume = Volume(
                                value = 0.0,
                                text = "0"
                            ),
                            key = Key.Dot
                        )

                        coVerify {
                            exchangeRepository.cacheVolume(
                                Volume(
                                    value = 0.0,
                                    text = "0."
                                )
                            )
                        }
                    }
                }
                and("volume is not empty") {
                    then("should append . to volume") {
                        updateVolumeUseCase(
                            asset = btcAsset,
                            volume = Volume(
                                value = 123.0,
                                text = "123"
                            ),
                            key = Key.Dot
                        )

                        coVerify {
                            exchangeRepository.cacheVolume(
                                Volume(
                                    value = 123.0,
                                    text = "123."
                                )
                            )
                        }
                    }
                }
            }
        }
    }
})