import wget

years = [
    "2014",
    "2015",
    "2016",
    "2017",
    "2018",
    "2019",
    "2020",
    "2021"
    ]

months = [
    "01",
    "02",
    "03",
    "04",
    "05",
    "06",
    "07",
    "08",
    "09",
    "10",
    "11",
    "12"
]

days = [
    "01",
    "02",
    "03",
    "04",
    "05",
    "06",
    "07",
    "08",
    "09",
    "10",
    "11",
    "12",
    "13",
    "14",
    "15",
    "16",
    "17",
    "18",
    "19",
    "20",
    "21",
    "22",
    "23",
    "24",
    "25",
    "26",
    "27",
    "28",
    "29",
    "30",
    "31"
]

url=""
date=""

for year in years:
    for month in months:
        for day in days:
            date = year + month + day
            url = "https://s3-eu-west-1.amazonaws.com/public.bitmex.com/data/trade/" + date + ".csv.gz"
            try:
                wget.download(url, './' + date + '.csv.gz')
                print("Dowloaded " + date)
            except:
                print("Did not find " + date)
            

