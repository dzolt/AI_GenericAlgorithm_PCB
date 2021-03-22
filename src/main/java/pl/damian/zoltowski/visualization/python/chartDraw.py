#!/bin/python3
import json
import sys
import matplotlib.pyplot as plt

class ChartDrawer:
    def __init__(self, data: dict):
        self.pcbWidth = data['pcbWidth']
        self.pcbHeight = data['pcbHeight']
        self.populationSize = data['populationSize']
        self.populationGenres = data['populationGenres']
        self.crossOverProbability = data['crossOverProbability']
        self.mutationProbability = data['mutationProbability']
        self.mutationMaxMoveSegment = data['mutationMaxMoveSegment']
        self.intersectionPenalty = data['intersectionPenalty']
        self.lengthOutOfBoardPenalty = data['lengthOutOfBoardPenalty']
        self.segmentsOutOfBoardPenalty = data['segmentsOutOfBoardPenalty']
        self.lengthPenalty = data['lengthPenalty']
        self.operator_name = data['selectionOP']
        self.maximums = []
        self.minimums = []
        self.avgs = []
        for max in data['maximums']:
            self.maximums.append(max)
        for min in data['minimums']:
            self.minimums.append(min)
        for avg in data['avgs']:
            self.avgs.append(avg)


    def get_image(self):
        x_label = 'Liczba epok'
        y_label = 'Wartość funkcji przystosowania'
        x_data = list(range(0, self.populationGenres))
        y_data_maxis = self.maximums
        y_data_minis = self.minimums
        y_data_avgs = self.avgs


        plt.figure(figsize=(20, 12))
        plt.subplots_adjust(bottom=0.25, top=0.95)

        # put maxis line
        plt.plot(x_data, y_data_maxis, label='Najepszy osobnik')
        # put minis line
        plt.plot(x_data, y_data_minis, label='Najgorszy osobnik')
        # put avgs line
        plt.plot(x_data, y_data_avgs, label='Średni osobnik')



        # name x and y labels
        plt.xlabel(x_label)
        plt.ylabel(y_label)

        # display title and legend
        plt.title('Zestawienie 16 generacji najlepszych, najgorszych i średnich osobników')
        plt.legend()

        info_1, info_2, info_3 = self.create_data_info()
        props = dict(edgecolor='black', boxstyle='round,pad=1', facecolor='white')

        # 1
        plt.text(0.0, -0.3, info_1, bbox=props, verticalalignment='top')
        # 2
        plt.text(2.3, -0.3, info_2, bbox=props, verticalalignment='top')
        # 3
        plt.text(5.5, -0.3, info_3, verticalalignment='top', bbox=props)
        # define fig size
        return plt

    def create_data_info(self):
        data_info_1 = "PCB rozmiar: {}x{}\nRozmiar pop.: {}\nLiczba pokoleń: {}"
        data_info_1 = data_info_1.format(self.pcbWidth, self.pcbHeight, self.populationSize, self.populationGenres)
        wages = [self.intersectionPenalty, self.segmentsOutOfBoardPenalty, self.lengthOutOfBoardPenalty, self.lengthPenalty]
        data_info_2 = "Wagi: {}\nOperator sel.: {}\nPrawd. krzyżowania:: {}%"
        data_info_2 = data_info_2.format(wages, self.operator_name, self.crossOverProbability * 100)

        data_info_3 = "Prawd. mutacji: {}%\nSiła mutacji: {}\n"
        data_info_3 = data_info_3.format(self.mutationProbability * 100, self.mutationMaxMoveSegment)

        return data_info_1, data_info_2, data_info_3





def run_programm():
    def usage():
        print("usage: generate.py <input (*.json)> [<output> (*.png)]")
        sys.exit(1)
    argv = sys.argv[1:]
    output_path = ''
    if argv:
        if not(json_path := argv.pop(0)).lower().endswith('.json'):
            usage()
        if argv:
            output_path = argv.pop(0)
        if argv:
            usage()
    else:
        usage()

    with open(json_path, 'r') as json_file:
        drawer = ChartDrawer(json.load(json_file))
        image = drawer.get_image()

    if output_path:
        image.savefig(output_path)
    else:
        image.show()
    sys.exit(0)


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    run_programm()

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
