import numpy as np
import pandas as pd
import glob
import matplotlib.pyplot as plt
import scipy as sp


# Get all important data from the csv's
class Measurement:
    def __init__(self, filename):
        data = pd.read_csv(filename)
        
        self.name = filename
        self.delta_t = data['Delta'][2:-1].to_numpy()/1000
        
        energy_key = "PACKAGE_ENERGY (J)"
        if "CPU_ENERGY (J)" in data.columns:
            energy_key  = "CPU_ENERGY (J)"
        elif "PACKAGE_ENERGY (W)" in data.columns:
            energy_key = "PACKAGE_ENERGY (W)"
        elif "SYSTEM_POWER (Watts)" in data.columns:
            energy_key = "SYSTEM_POWER (Watts)"
        elif "CPU_POWER (Watts)" in data.columns:
            energy_key = "CPU_POWER (Watts)"

        if energy_key == "PACKAGE_ENERGY (J)" or energy_key == "CPU_ENERGY (J)":
            self.delta_J = data[energy_key].diff()[2:-1].to_numpy()
            self.watts = self.delta_J / self.delta_t
        else:
            self.watts = data[energy_key][2:-1].to_numpy()
            self.delta_J = self.watts * self.delta_t

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
        ax.set_ylabel('Power consumption [Watt]')
        ax.set_title('Power measurements null setting')
    
    def plot_threads_joule(self, ax):
        null_watts = np.mean([m.avg_W for m in self.null_measurements])
        
        for i, (thread_type, thread_measurements) in enumerate(self.measurements.items()):
            thread_data = np.array([m.total_J - m.total_t*null_watts for m in thread_measurements])
            thread_amount = thread_type.split('_')[-1]
            
            ax.boxplot(thread_data, positions=[i], tick_labels=[thread_amount], label=f'{thread_amount} threads: {round(np.mean(thread_data),1)} J')
            ax.violinplot(thread_data, positions=[i], showextrema=False)
        
        ax.legend(title='Mean consumption')
        ax.set_ylabel('Energy consumption [J]')
        ax.set_xlabel('Number of threads')
        ax.set_title('Energy measurements varying thread amount')
    
    def plot_threads_watts(self, ax):
        null_watts = np.mean([m.avg_W for m in self.null_measurements])
        
        for i, (thread_type, thread_measurements) in enumerate(self.measurements.items()):
            thread_data = np.array([m.avg_W - null_watts for m in thread_measurements])
            thread_amount = thread_type.split('_')[-1]
            
            ax.boxplot(thread_data, positions=[i], tick_labels=[thread_amount], label=f'{thread_amount} threads: {round(np.mean(thread_data),1)} Watt')
            ax.violinplot(thread_data, positions=[i], showextrema=False)
        
        ax.legend(title='Mean consumption')
        ax.set_ylabel('Power consumption [Watt]')
        ax.set_xlabel('Number of threads')
        ax.set_title('Power measurements varying thread amount')


def main():
    null_folder = 'null'
    thread_folders = glob.glob(f'threads_*')

    analysis = Database(null_folder, thread_folders)

    null_watts = np.mean([m.avg_W for m in analysis.null_measurements])

    print("Testing if collected data follows normal distribution with Shapiro")
    for folder in [null_folder] + thread_folders:
        if folder == null_folder:
            folder_data = [m.total_J - m.total_t*null_watts for m in analysis.null_measurements]
        else:
            folder_data = [m.total_J - m.total_t*null_watts for m in analysis.measurements[folder]]
        _, shapiro_p = sp.stats.shapiro(folder_data)
        if shapiro_p >= 0.05:
            print(f'{folder}: Data is normal \np_value = {round(shapiro_p,5)}\n')
        else:
            print(f'{folder}: Data is NOT normal \np_value = {round(shapiro_p,5)}\n')

    fig, ax = plt.subplots(1,3, figsize=(12,4))

    analysis.plot_null(ax[0])
    analysis.plot_threads_joule(ax[1])
    analysis.plot_threads_watts(ax[2])

    plt.show()


if __name__=='__main__':
    main()
