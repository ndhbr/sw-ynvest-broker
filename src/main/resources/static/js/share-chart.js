(function () {
    'use strict'
    const labels = [];
    const data = [];

    const days = [
        'So', 'Mo', 'Di', 'Mi', 'Do',
        'Fr', 'Sa'
    ];

    if (type != null && marketValues != null && marketValues.length > 0) {
        for (const value of marketValues) {
            const date = new Date(value.timestamp);
            data.push(value.unitPrice);

            switch (type) {
                case "DAY":
                    labels.push((date.getHours() < 10 ? '0' : '') + date.getHours() + ':00');
                    break;
                case "WEEK":
                    labels.push(days[date.getDay()]);
                    break;
                case "MONTH":
                    labels.push(date.getDate() + '.' +
                        ((date.getMonth()+1) < 10 ? '0' : '') + (date.getMonth()+1));
                    break;
                case "YEAR":
                    const offset = date.getTimezoneOffset();
                    const d = new Date(date.getTime() - (offset * 60 * 1000));
                    labels.push(d.toISOString().split('T')[0]);
                    break;
            }
        }
    }

    // Chart
    const ctx = document.getElementById('stock-chart')
    const stockChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                data: data,
                lineTension: 0,
                backgroundColor: 'transparent',
                borderColor: '#125c00',
                borderWidth: 4,
                pointBackgroundColor: '#125c00'
            }]
        },
        options: {
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: false
                    }
                }]
            },
            legend: {
                display: false
            }
        }
    })
})()