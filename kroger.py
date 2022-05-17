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

options = Options()
options.headless = True

driver = webdriver.Chrome(executable_path='C:/Users/qwert/chromedriver/chromedriver.exe')

URL = 'https://www.kroger.com/'

def existsid(id):
    try:
        driver.find_element_by_id(id)
    except NoSuchElementException:
        return False
    return True

def existsclass(classname):
    try:
        driver.find_element_by_class_name(classname)
    except NoSuchElementException:
        return False
    return True


pricebook = {}
itemid = 101
df = pd.read_csv('grocerylist.csv')
grocerylist = df['Column1'].to_numpy()

if __name__ == '__main__':
    driver.get(URL)
    time.sleep(4)
    for item in grocerylist:
        currentitem = {}
        if existsid('SearchBar-input'):
            driver.find_element_by_id('SearchBar-input').clear()
            driver.find_element_by_id('SearchBar-input').send_keys(item)
            driver.find_element_by_id('SearchBar-input').submit()
        time.sleep(8)
        currentitem['id'] = itemid
        currentitem['product_name'] = driver.find_element_by_xpath('//*[@data-qa="cart-page-item-description"]').text
        currentitem['unit'] = driver.find_element_by_xpath('//*[@data-qa="cart-page-item-description"]').text
        link = driver.find_element_by_class_name("kds-Link kds-Link--inherit kds-Link--implied").get_attribute("href")
        link = "https://kroger.com" + link
        currentitem['product_link'] = link
        pricebook[item] = currentitem
        itemid = itemid + 1
        time.sleep(2)
    json_object = json.dumps(pricebook, indent = 4)
    with open('safeway_prices.json', 'w') as outfile:
        outfile.write(json_object)

