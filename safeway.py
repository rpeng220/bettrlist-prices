import pandas as pd
from selenium import webdriver
from selenium.common.exceptions import NoSuchElementException
from bs4 import BeautifulSoup
from datetime import datetime
import time
import os.path
from os import path
import numpy as np
from selenium.webdriver.chrome.options import Options
import json

options = Options()
options.headless = True

driver = webdriver.Chrome(executable_path='C:/Users/qwert/chromedriver/chromedriver.exe')

URL = 'https://safeway.com'

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

def existsxpath(xpath):
    try:
        driver.find_element_by_xpath(xpath)
    except NoSuchElementException:
        return False
    return True

pricebook = {}
itemid = 100001
df = pd.read_csv('grocerylist.csv')
grocerylist = df['Column1'].to_numpy()


if __name__ == '__main__':
    driver.get(URL)
    time.sleep(10)
    for item in grocerylist:
        if existsid('skip-main-content'):
            driver.find_element_by_id('skip-main-content').clear()
            driver.find_element_by_id('skip-main-content').send_keys(item)
            driver.find_element_by_id('skip-main-content').submit()
        time.sleep(4)
        productnames = driver.find_elements_by_class_name('product-title__name')
        unitprices = driver.find_elements_by_class_name('product-title__qty')
        prices = driver.find_elements_by_class_name('product-price__saleprice product-price__discounted-price')
        for i in np.arange(len(productnames)):
            counter = str(i + 1)
            currentitem = {'id': itemid, 'store': 'Safeway',
                           'product_name': productnames[i].text, 'unit_price': unitprices[i].text,
                           'price': prices[i].get_attribute("innerText"),
                           'product_link': productnames[i].get_attribute("href"), 'search_item': item}
            pricebook[itemid] = currentitem
            itemid += 1
        #
        # currentitem['id'] = itemid
        # currentitem['store'] = 'Safeway'
        # currentitem['price'] = driver.find_element_by_class_name('product-price ').get_attribute("innerText")
        # currentitem['product_name'] = driver.find_element_by_class_name('product-title').text
        # currentitem['unit_price'] = driver.find_element_by_class_name('product-price-qty').text
        # currentitem['product_link'] = driver.find_element_by_xpath('//product-item-v2//a').get_attribute("href")
        # pricebook[item] = currentitem
        # itemid += 1
        time.sleep(1)
    json_object = json.dumps(pricebook, indent = 4)
    with open('safeway_prices.json', 'w') as outfile:
        outfile.write(json_object)

