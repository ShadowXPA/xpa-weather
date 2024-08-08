$(() => {
    const body = $('body')
    const form = $('#weather-form')
    let theme = localStorage.getItem('theme') ?? 'day'
    let units = localStorage.getItem('units') ?? 'metric'

    const setLoading = (loading) => {
        if (loading) {
            $('#loading').removeClass('d-none')
        } else {
            $('#loading').addClass('d-none')
        }
    }

    $('[name=theme]').on('click', (event) => {
        theme = $('[name=theme]:checked').val()
        localStorage.setItem('theme', theme)
        body.removeClass()
        body.addClass(theme)
    })

    $('[name=units]').on('click', (event) => {
        units = $('[name=units]:checked').val()
        localStorage.setItem('units', units)
    })

    $(`[name=theme][value=${theme}]`).click()
    $(`[name=units][value=${units}]`).click()

    $('#btn-location').on('click', (event) => {
        event.preventDefault()

        $('#geocoding-api-key').val($('#geocoding-api-key').val().trim())
        const geocodingApiKey = $('#geocoding-api-key').val()

        if (geocodingApiKey) {
            if (navigator.geolocation) {
                setLoading(true)
                navigator.geolocation.getCurrentPosition(position => {
                        const latitude = position.coords.latitude
                        const longitude = position.coords.longitude

                        fetch(`/api/v1/geocoding?geo-api-key=${geocodingApiKey}&lat=${latitude}&lon=${longitude}`)
                            .then(response => {
                                if (!response.ok) {
                                    throw new Error('Network response was not ok');
                                }

                                return response.json()
                            })
                            .then(data => {
                                $('#location').val(data.address)
                                setLoading(false)
                            })
                            .catch(error => {
                                setLoading(false)
                            })
                    },
                    error => {
                        if (error.code === GeolocationPositionError.POSITION_UNAVAILABLE) {
                            $('#btn-location').prop('disabled', true)
                            $('#btn-location').off('click')
                            alert('You can not use this feature... Position is unavailable')
                        }

                        setLoading(false)
                    }
                )
            } else {
                $('#btn-location').prop('disabled', true)
                $('#btn-location').off('click')
                alert('You can not use this feature... No geolocation on this device')
            }
        } else {
            $('#geocoding-api-key').focus()
            alert('You need to add a Geocoding API key')
        }
    })

    $('#btn-weather').on('click', (event) => {
        setLoading(true)

        if (!form[0].checkValidity()) {
            event.preventDefault()
            event.stopPropagation()
            setLoading(false)
        }

        form.addClass('was-validated')
    })
})
