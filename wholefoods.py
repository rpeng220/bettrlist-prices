import pandas as pd
from selenium import webdriver
from selenium.common.exceptions import NoSuchElementException
from bs4 import BeautifulSoup
from datetime import datetime
import time
import os.path
from os import path
from selenium.webdriver.chrome.options import Options
import json
import numpy as np

driver = webdriver.Chrome(executable_path='C:/Users/qwert/chromedriver/chromedriver.exe')

URL = 'https://www.wholefoodsmarket.com/'

itemid = 400001

pricebook = {}

df = pd.read_csv('grocerylist.csv')
grocerylist = df['Column1'].to_numpy()

def existsclass(classname):
    try:
        driver.find_element_by_class_name(classname)
    except NoSuchElementException:
        return False
    else:
        return True


if __name__ == '__main__':
    driver.get(URL)
    time.sleep(20)
    for item in grocerylist:
        driver.get('https://www.wholefoodsmarket.com/search?text=' + item)
        time.sleep(5)
        if existsclass('regular_price'):
            productnames = driver.find_elements_by_xpath('//*[@data-testid="product-tile-name"]')
            prices = driver.find_elements_by_class_name('regular_price')
            links = driver.find_elements_by_xpath('//*[@data-testid="product-tile-link"]')
            for i in np.arange(len(productnames)):
                currentitem = {'id': itemid, 'search_name': item, 'store': 'Whole Foods', 'unit_price': '',
                               'product_name': productnames[i].text, 'price': prices[i].text,
                               'product_link': links[i].get_attribute('href')}
                pricebook[itemid] = currentitem
                itemid = itemid + 1
        time.sleep(2)
    json_object = json.dumps(pricebook, indent = 4)
    with open('wholefoods_prices.json', 'w') as outfile:
        outfile.write(json_object)

