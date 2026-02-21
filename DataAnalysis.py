import numpy as np
import pandas as pd
import glob
import matplotlib.pyplot as plt


# Get all important data from the csv's
class Measurement:
    def __init__(self, filename):
        
        data = pd.read_csv(filename)
        
        self.name = filename.rstrip('.csv').replace('/', ' ').replace('results_', '')
        self.delta_J = data['PACKAGE_ENERGY (J)'].diff()[2:-1].to_numpy()
        self.delta_t = data['Delta'][2:-1].to_numpy()/1000
        self.watt = self.delta_J / self.delta_t

        self.total_J = np.sum(self.delta_J)
        self.total_t = np.sum(self.delta_t)
        self.avg_W = self.total_J/self.total_t

        del data


# Stores all measurements and makes plots
class Database:
    def __init__(self, null_folder, thread_folder_list):
        null_files = glob.glob(f'{null_folder}/results_*.csv')
        self.null_measurements = [Measurement(null_file) for null_file in null_files]

        self.measurements = {}
        for thread_folder in thread_folder_list:
            thread_files = glob.glob(f'{thread_folder}/results_*.csv')
            self.measurements[thread_folder] = [Measurement(thread_file) for thread_file in thread_files]
    
    def plot_null(self, ax):
        Watt_data = np.array([m.avg_W for m in self.null_measurements])

        ax.boxplot(Watt_data, positions=[1], tick_labels=['Null measurements'], label=f'mean: {round(np.mean(Watt_data),3)} W')
        ax.violinplot(Watt_data, positions=[1], showextrema=False)
        
        ax.legend()
        ax.set_ylabel('Energie consumption [Watt]')
        ax.set_title('Energy measurements null setting')
    
    def plot_threads(self, ax):
        null_watts = np.mean([m.avg_W for m in self.null_measurements])
        
        for i, (thread_type, thread_measurements) in enumerate(self.measurements.items()):
            thread_data = np.array([m.total_J - m.total_t*null_watts for m in thread_measurements])
            thread_amount = thread_type.split('_')[-1]
            
            ax.boxplot(thread_data, positions=[i], tick_labels=[thread_amount], label=f'{thread_amount} threads: {round(np.mean(thread_data),3)} J')
            ax.violinplot(thread_data, positions=[i], showextrema=False)
        
        ax.legend(title='Mean consumption')
        ax.set_ylabel('Energie consumption [J]')
        ax.set_xlabel('Number of threads')
        ax.set_title('Energie measurements varying thread amount')


def main():
    null_folder = 'null_measurements'
    thread_folders = ['threads_1', 'threads_5', 'threads_10']

    analysis = Database(null_folder, thread_folders)

    fig, ax = plt.subplots(1,2, figsize=(12,5))

    analysis.plot_null(ax[0])
    analysis.plot_threads(ax[1])

    plt.show()


if __name__=='__main__':
    main()
