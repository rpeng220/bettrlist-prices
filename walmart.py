import pandas as pd
from selenium import webdriver
from selenium.common.exceptions import NoSuchElementException
from bs4 import BeautifulSoup
from datetime import datetime
import time
import undetected_chromedriver as uc
import os.path
from os import path
from selenium.webdriver.chrome.options import Options
import json
import numpy as np


URL = 'https://www.instacart.com/'



pricebook = {}
itemid = 100001
df = pd.read_csv('grocerylist.csv')
grocerylist = df['Column1'].to_numpy()


def existsxpath(xpath):
    try:
        driver.find_element_by_xpath(xpath)
    except NoSuchElementException:
        return False
    else:
        return True


if __name__ == '__main__':
    driver = uc.Chrome(version_main=101)
    driver.get(URL)
    time.sleep(5)
    for item in grocerylist:
        driver.get('https://www.instacart.com/store/walmart/search/' + item)
        time.sleep(7)
        if existsxpath("//span[contains(text(),'$')]"):
            productnames = driver.find_elements_by_xpath("//span[contains(text(),'$')]/following-sibling::div[@class='css-9vf613']")
            prices = driver.find_elements_by_xpath("//span[contains(text(),'$')]")
            links = driver.find_elements_by_xpath("//*[@aria-label='item']//*[@href]")
            for i in np.arange(len(productnames)):
                counter = str(i+1)
                if existsxpath("(//div[@class='css-9vf613'])[" + counter + "]/following-sibling::div[1]"):
                    quantity = driver.find_element_by_xpath("(//div[@class='css-9vf613'])[" + counter + "]/following-sibling::div[1]").text
                else:
                    quantity = ''
                currentitem = {'id': itemid, 'store': 'Walmart',
                               'product_name': productnames[i].get_attribute('innerHTML') + ' ' + quantity,
                               'price': prices[i].text, 'product_link': links[i].get_attribute("href"),
                               'unit_price': '', 'search_item': item}
                pricebook[itemid] = currentitem
                itemid = itemid + 1
                time.sleep(1)
    json_object = json.dumps(pricebook, indent = 4)
    with open('walmart_prices.json', 'w') as outfile:
        outfile.write(json_object)

