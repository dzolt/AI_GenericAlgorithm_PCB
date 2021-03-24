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


        plt.figure(figsize=(18, 10))

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
        plt.title('Zestawienie 10 generacji najlepszych, najgorszych i średnich osobników')
        plt.legend()


        return plt


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
