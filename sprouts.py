import pandas as pd
from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.common.exceptions import NoSuchElementException
from bs4 import BeautifulSoup
from datetime import datetime
import time
import os.path
from os import path
from selenium.webdriver.chrome.options import Options
import json
# import undetected_chromedriver as uc

driver = webdriver.Chrome(executable_path='C:/Users/qwert/chromedriver/chromedriver.exe')
URL = 'https://shop.sprouts.com/'



pricebook = {}
itemid = 301
df = pd.read_csv('grocerylist.csv')
grocerylist = df['Column1'].to_numpy()

def existsclass(classname):
    try:
        driver.find_element_by_class_name(classname)
    except NoSuchElementException:
        return False
    else:
        return True

def existsxpath(xpath):
    try:
        driver.find_element_by_xpath(xpath)
    except NoSuchElementException:
        return False
    else:
        return True

if __name__ == '__main__':
    driver.get(URL)
    for item in grocerylist:
        currentitem = {}
        driver.get('https://shop.sprouts.com/search?search_term=' + item + '&search_is_autocomplete=false')
        time.sleep(10)
        currentitem['id'] = itemid
        currentitem['store'] = 'Sprouts'
        # currentitem['store'] = driver.find_element_by_xpath("//*[@data-test='store-button']/span/span").text
        currentitem['product_link'] = 'https://shop.sprouts.com/search?search_term=' + item + '&search_is_autocomplete=false'
        if existsclass('cell-title-text'):
            currentitem['product_name'] = driver.find_element_by_class_name('cell-title-text').text + " " + \
                                          driver.find_element_by_class_name('cell-product-size').text
            currentitem['price'] = driver.find_element_by_xpath('//*[@data-test="amount"]/span').text
            if existsxpath("//*[@data-test='per-unit-price']"):
                unitprice = driver.find_element_by_xpath("//*[@data-test='per-unit-price']")
                currentitem['unit_price'] = unitprice.text
            pricebook[item] = currentitem
        itemid = itemid + 1
        time.sleep(1)
    json_object = json.dumps(pricebook, indent = 4)
    with open('sprouts_prices.json', 'w') as outfile:
        outfile.write(json_object)

